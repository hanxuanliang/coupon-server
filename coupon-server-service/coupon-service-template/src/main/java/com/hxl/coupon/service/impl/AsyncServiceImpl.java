package com.hxl.coupon.service.impl;

import com.google.common.base.Stopwatch;
import com.hxl.coupon.constant.Constant;
import com.hxl.coupon.dao.CouponTemplateDao;
import com.hxl.coupon.entity.CouponTemplate;
import com.hxl.coupon.service.IAsyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @Description: 异步服务接口实现
 * @Author: hanxuanliang
 * @Date: 2019/12/17 21:12
 */
@Slf4j
@Service
public class AsyncServiceImpl implements IAsyncService {

    /** CouponTemplateDao */
    private final CouponTemplateDao templateDao;
    /** 注入Redis模板类 */
    private final StringRedisTemplate redisTemplate;

    @Autowired
    public AsyncServiceImpl(CouponTemplateDao templateDao, StringRedisTemplate redisTemplate) {
        this.templateDao = templateDao;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 根据模板异步创建优惠券码
     *
     * @param template 优惠券模板实体
     * @date: 2019/12/18 14:02
     */
    @Async("getAsyncExecutor")
    @Override
    @SuppressWarnings("all")
    public void asyncContructCouponTemplate(CouponTemplate template) {
        Stopwatch watch = Stopwatch.createStarted();
        Set<String> couponCodes = buildCouponCode(template);

        // 插入redis中
        String redisKey = String.format("%s%s", Constant.RedisPrefix.COUPON_TEMPLATE, template.getId().toString());
        log.info("Push CouponCode To Redis: {}",
                redisTemplate.opsForList().rightPushAll(redisKey, couponCodes));
        // 把template的状态修改，并插入到mysql中
        template.setAvailable(true);
        templateDao.save(template);
        watch.stop();
        log.info("Construct CouponCode By Template Cost: {}ms",
                watch.elapsed(TimeUnit.MILLISECONDS));
        log.info("CouponTemplate({}) is Available!", template.getId());
    }

    /**
     * 构造优惠券码
     * 18位：
     * 前4位：产品线+类型
     * 中间6位：日期随机（200301）
     * 后8位：0~9 随机数构成
     * @param template 实体类
     * @return Set<String> 生成 template.count 相同个数的优惠券码
     * @date: 2019/12/18 14:03
     */
    @SuppressWarnings("all")
    private Set<String> buildCouponCode(CouponTemplate template) {
        // 计时器
        Stopwatch watch = Stopwatch.createStarted();
        Set<String> result = new HashSet<>(template.getCount());
        // 前面4位
        String prefix4 = template.getPlatformLineCategory().getCode().toString()
                + template.getCouponCategory().getCode();
        // 中间6位（日期）
        String date = new SimpleDateFormat("yyMMdd").format(template.getCreateTime());
        for (int i = 0; i != template.getCount(); ++i) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }
        // 可能会重复，加入set的时候就会 < 该有的大小，所以此处需要再一次的生成
        while (result.size() < template.getCount()) {
            result.add(prefix4 + buildCouponCodeSuffix14(date));
        }
        assert result.size() == template.getCount();
        watch.stop();
        log.info("Build Coupon Code Cost: {}ms", watch);
        return result;
    }

    // 构造优惠劵码的后14位（传入日期 + 以及自己生成的0~9的随机数）
    private String buildCouponCodeSuffix14(String date) {
        char[] bases = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'};
        // 中间6位
        List<Character> characters = date.chars()
                .mapToObj(e -> (char) e).collect(Collectors.toList());
        Collections.shuffle(characters);
        String mid6 = characters.stream().map(Object::toString).collect(Collectors.joining());
        // 后八位: 从bases里面选取一位(也就是第一位不为0)，其余的则随机选择7位即可
        String suffix8 = RandomStringUtils.random(1, bases)
                + RandomStringUtils.randomNumeric(7);
        return mid6 + suffix8;
    }
}
