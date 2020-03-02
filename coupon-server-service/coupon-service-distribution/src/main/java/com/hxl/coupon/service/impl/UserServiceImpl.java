package com.hxl.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.hxl.coupon.client.SettlementClient;
import com.hxl.coupon.client.TemplateClient;
import com.hxl.coupon.constant.Constant;
import com.hxl.coupon.constant.CouponStatus;
import com.hxl.coupon.dao.CouponDao;
import com.hxl.coupon.entity.Coupon;
import com.hxl.coupon.exception.CouponException;
import com.hxl.coupon.service.IRedisService;
import com.hxl.coupon.service.IUserService;
import com.hxl.coupon.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * <h1>用户服务相关的接口实现</h1>
 *  所有的操作过程, 状态都保存在 Redis 中, 并通过 Kafka 把消息传递到 MySQL 中
 *  为什么使用 Kafka, 而不是直接使用 SpringBoot 中的异步处理 ?  高可用的异步队列
 * @Author: hanxuanliang
 * @Date: 2020/3/2 15:25
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {

    /** Coupon Dao */
    private final CouponDao couponDao;

    /** Redis 服务 */
    private final IRedisService redisService;

    /** 模板微服务客户端 */
    private final TemplateClient templateClient;

    /** 结算微服务客户端 */
    private final SettlementClient settlementClient;

    /** Kafka 客户端 */
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public UserServiceImpl(CouponDao couponDao, IRedisService redisService,
                           TemplateClient templateClient, SettlementClient settlementClient,
                           KafkaTemplate<String, String> kafkaTemplate) {
        this.couponDao = couponDao;
        this.redisService = redisService;
        this.templateClient = templateClient;
        this.settlementClient = settlementClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * <h2>根据用户 id 和状态 查询优惠券记录</h2>
     * @param userId 用户 id
     * @param status 优惠券状态
     * @return {@link Coupon}s
     */
    @Override
    public List<Coupon> findCouponsByStatus(Long userId, Integer status) throws CouponException {

        List<Coupon> curCached = redisService.getCachedCoupons(userId, status);
        List<Coupon> preTarget;

        if (CollectionUtils.isNotEmpty(curCached)) {
            log.debug("coupon cache is not empty: {}, {}", userId, status);
            preTarget = curCached;
        } else {
            log.debug("coupon cache is empty, get coupon from db: {}, {}",
                    userId, status);
            List<Coupon> dbCoupons = couponDao.findAllByUserIdAndStatus(userId, CouponStatus.of(status));
            // 如果数据库中没有记录, 直接返回就可以, Cache 中已经加入了一张无效的优惠券
            if (CollectionUtils.isEmpty(dbCoupons)) {
                log.debug("current user do not have coupon: {}, {}", userId, status);
                return dbCoupons;
            }

            // 填充 dbCoupons的 templateSDK 字段
            // 【通过模板微服务系统获取模板信息，getData()拿到模板信息返回对象的data数据】
            Map<Integer, CouponTemplatesdk> id2Templatesdk = templateClient.findids2Templatesdk(
                            dbCoupons.stream()
                                    .map(Coupon::getTemplateId)
                                    .collect(Collectors.toList())
                    ).getData();
            dbCoupons.forEach(dc -> dc.setTemplatesdk(id2Templatesdk.get(dc.getTemplateId())));
            // 数据库中存在记录
            preTarget = dbCoupons;
            // 将记录写入 Cache
            redisService.addCouponToCache(userId, preTarget, status);
        }

        // 将无效优惠券剔除【可能会存在无效的优惠券，缓存穿透的 “-1” 】
        preTarget = preTarget.stream().filter(c -> c.getId() != -1).collect(Collectors.toList());
        // 如果当前获取的是可用优惠券, 还需要做对已过期优惠券的延迟处理
        if (CouponStatus.of(status) == CouponStatus.USABLE) {
            CouponClassify classify = CouponClassify.classify(preTarget);
            // 如果已过期状态不为空, 需要做延迟处理
            if (CollectionUtils.isNotEmpty(classify.getExpired())) {
                log.info("Add Expired Coupons To Cache From FindCouponsByStatus: {}, {}", userId, status);
                redisService.addCouponToCache(
                        userId, classify.getExpired(),
                        CouponStatus.EXPIRED.getCode()
                );
                // 发送到 kafka 中做异步处理【topic，序列化好的字符串{status，ids}】
                kafkaTemplate.send(
                        Constant.TOPIC,
                        JSON.toJSONString(new CouponKafkaMessage(
                                CouponStatus.EXPIRED.getCode(),
                                classify.getExpired().stream()
                                        .map(Coupon::getId)
                                        .collect(Collectors.toList())
                        ))
                );
            }
            return classify.getUsable();
        }
        return preTarget;
    }

    /**
     * <h2>根据用户 id 查找当前可以领取的优惠券模板</h2>
     * @param userId 用户 id
     * @return {@link CouponTemplatesdk}s
     */
    @Override
    public List<CouponTemplatesdk> findAvailableTemplate(Long userId) throws CouponException {

        long curTime = System.currentTimeMillis();
        List<CouponTemplatesdk> templatesdks = templateClient.findAllUsableTemplate().getData();

        log.debug("Find All Template(From TemplateClient) Count: {}",
                templatesdks.size());

        // 过滤过期的优惠券模板【因为过期服务是一个定时服务，但是可能会有延时，所以在这验证一下过期时间】
        templatesdks = templatesdks.stream()
                .filter(t -> t.getRule().getExpiration().getDeadline() > curTime)
                .collect(Collectors.toList());

        log.info("Find Usable Template Count: {}", templatesdks.size());

        // key 是 TemplateId
        // value 中的 first 是 Template limitation[领取次数上限], second 是优惠券模板
        // 【在这里处理优惠券领取上限，多余的优惠券就不可见】
        Map<Integer, Pair<Integer, CouponTemplatesdk>> limit2Template = new HashMap<>(templatesdks.size());
        templatesdks.forEach(
                t -> limit2Template.put(t.getId(), Pair.of(t.getRule().getLimitation(), t))
        );

        List<CouponTemplatesdk> result = new ArrayList<>(limit2Template.size());
        List<Coupon> userUsableCoupons = findCouponsByStatus(userId, CouponStatus.USABLE.getCode());

        log.debug("Current User Has Usable Coupons: {}, {}", userId, userUsableCoupons.size());

        // 将用户当前可用的优惠券转换为map结构：key 是 TemplateId
        Map<Integer, List<Coupon>> templateId2Coupons = userUsableCoupons
                .stream()
                .collect(Collectors.groupingBy(Coupon::getTemplateId));

        // 根据 Template 的 Rule 判断是否可以领取优惠券模板【所有返回优惠券模板都在 limit2Template 中】
        limit2Template.forEach((k, v) -> {
            int limitation = v.getLeft();
            CouponTemplatesdk templatesdk = v.getRight();
            // 用户本身已经拥有了这个优惠券模板，并且 数量超过了限制
            if (templateId2Coupons.containsKey(k) && templateId2Coupons.get(k).size() >= limitation) {
                return;
            }
            result.add(templatesdk);
        });
        return result;
    }

    /**
     * <h2>用户领取优惠券</h2>
     * 1. 从 TemplateClient 拿到对应的优惠券, 并检查是否过期
     * 2. 根据 limitation 判断用户是否可以领取
     * 3. save to db
     * 4. 填充 CouponTemplateSDK
     * 5. save to cache【以便让模板中有这个sdk信息】
     * @param request {@link AcquireTemplateRequest}
     * @return {@link Coupon}
     */
    @Override
    public Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException {

        Map<Integer, CouponTemplatesdk> id2Template =
                templateClient.findids2Templatesdk(Collections.singletonList(request.getTemplatesdk().getId())).getData();

        // 优惠券模板是需要存在的
        if (id2Template.size() <= 0) {
            log.error("Can Not Acquire Template From TemplateClient: {}",
                    request.getTemplatesdk().getId());
            throw new CouponException("Can Not Acquire Template From TemplateClient");
        }

        // 用户是否可以领取这张优惠券
        List<Coupon> userUsableCoupons = findCouponsByStatus(request.getUserId(), CouponStatus.USABLE.getCode());
        Map<Integer, List<Coupon>> templateId2Coupons = userUsableCoupons
                .stream()
                .collect(Collectors.groupingBy(Coupon::getTemplateId));

        if (templateId2Coupons.containsKey(request.getTemplatesdk().getId())
                && templateId2Coupons.get(request.getTemplatesdk().getId()).size() >=
                request.getTemplatesdk().getRule().getLimitation()) {
            log.error("Exceed Template Assign Limitation: {}",
                    request.getTemplatesdk().getId());
            throw new CouponException("Exceed Template Assign Limitation");
        }

        // 尝试去获取优惠券码
        String couponCode = redisService.tryToAcquireCouponCodeFromCache(request.getTemplatesdk().getId());
        if (StringUtils.isEmpty(couponCode)) {
            log.error("Can Not Acquire Coupon Code: {}",
                    request.getTemplatesdk().getId());
            throw new CouponException("Can Not Acquire Coupon Code");
        }

        Coupon newCoupon = new Coupon(
                request.getTemplatesdk().getId(), request.getUserId(),
                couponCode, CouponStatus.USABLE
        );
        newCoupon = couponDao.save(newCoupon);

        // 填充 Coupon 对象的 CouponTemplateSDK, 一定要在放入缓存之前去填充
        newCoupon.setTemplatesdk(request.getTemplatesdk());

        // 放入缓存中
        redisService.addCouponToCache(
                request.getUserId(), Collections.singletonList(newCoupon), CouponStatus.USABLE.getCode());

        return newCoupon;
    }

    /**
     * <h2>结算(核销)优惠券</h2>
     * 这里需要注意, 规则相关处理需要由 Settlement 系统去做, 当前系统仅仅做业务处理过程(校验过程)
     * @param info {@link SettlementInfo}
     * @return {@link SettlementInfo}
     */
    @Override
    public SettlementInfo settlement(SettlementInfo info) throws CouponException {
        // 当没有传递优惠券时, 直接返回商品总价
        List<SettlementInfo.CouponAndTemplateInfo> ctInfos = info.getCouponAndTemplateinfos();
        if (CollectionUtils.isEmpty(ctInfos)) {
            log.info("Empty Coupons For Settle.");
            double goodsSum = 0.0;
            for (GoodsInfo gi : info.getGoodsInfos()) {
                goodsSum += gi.getPrice() * gi.getCount();
            }
            // 没有优惠券也就不存在优惠券的核销, SettlementInfo 其他的字段不需要修改
            info.setCost(retain2Decimals(goodsSum));
        }
        // 校验传递的优惠券是否是用户自己的
        List<Coupon> coupons = findCouponsByStatus(info.getUserId(), CouponStatus.USABLE.getCode());
        // Function.identity(): 返回它本身
        Map<Integer, Coupon> id2Coupon = coupons.stream()
                .collect(Collectors.toMap(Coupon::getId, Function.identity()));
        if (MapUtils.isEmpty(id2Coupon) || !CollectionUtils.isSubCollection(
                ctInfos.stream()
                        .map(SettlementInfo.CouponAndTemplateInfo::getId).collect(Collectors.toList()),
                id2Coupon.keySet())) {
            log.error("{}", id2Coupon.keySet());
            log.error("{}", ctInfos.stream()
                    .map(SettlementInfo.CouponAndTemplateInfo::getId)
                    .collect(Collectors.toList()));
            log.error("User Coupon Has Some Problem, It Is Not SubCollection Of Coupons!");
            throw new CouponException("User Coupon Has Some Problem, It Is Not SubCollection Of Coupons!");
        }

        log.debug("Current Settlement Coupons Is User's: {}", ctInfos.size());

        List<Coupon> settleCoupons = new ArrayList<>(ctInfos.size());
        ctInfos.forEach(ci -> settleCoupons.add(id2Coupon.get(ci.getId())));

        // 通过结算服务获取结算信息
        SettlementInfo processedInfo = settlementClient.computeRule(info).getData();
        // 核销优惠券 getEmploy() 返回 True
        if (processedInfo.getEmploy() && CollectionUtils.isNotEmpty(processedInfo.getCouponAndTemplateinfos())) {
            log.info("Settle User Coupon: {}, {}", info.getUserId(),
                    JSON.toJSONString(settleCoupons));
            // 更新缓存，告诉缓存的状态为USED，已用
            redisService.addCouponToCache(info.getUserId(), settleCoupons, CouponStatus.USED.getCode());
            // 更新 db，更新DB的状态为已用
            kafkaTemplate.send(
                    Constant.TOPIC,
                    JSON.toJSONString(new CouponKafkaMessage(
                            CouponStatus.USED.getCode(),
                            settleCoupons.stream().map(Coupon::getId).collect(Collectors.toList())
                    ))
            );
        }
        return processedInfo;
    }

    /**
     * <h2>保留两位小数</h2>
     * */
    private double retain2Decimals(double value) {
        // BigDecimal.ROUND_HALF_UP 代表四舍五入
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
