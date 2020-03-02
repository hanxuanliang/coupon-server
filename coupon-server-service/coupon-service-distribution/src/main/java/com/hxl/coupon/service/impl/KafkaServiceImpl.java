package com.hxl.coupon.service.impl;

import com.alibaba.fastjson.JSON;
import com.hxl.coupon.constant.Constant;
import com.hxl.coupon.constant.CouponStatus;
import com.hxl.coupon.dao.CouponDao;
import com.hxl.coupon.entity.Coupon;
import com.hxl.coupon.service.IKafkaService;
import com.hxl.coupon.vo.CouponKafkaMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


/**
 * @Description: Kafka 相关的服务接口实现
 * 核心思想: 是将 Cache 中的 Coupon 的状态变化同步到 DB 中
 * @Author: hanxuanliang
 * @Date: 2020/3/2 14:41
 */
@Slf4j
@Component
public class KafkaServiceImpl implements IKafkaService {

    /** Coupon Dao */
    private final CouponDao couponDao;

    @Autowired
    public KafkaServiceImpl(CouponDao couponDao) { this.couponDao = couponDao; }

    /**
     * <h2>消费优惠券 Kafka 消息</h2>
     * @param record {@link ConsumerRecord}
     * 而且这个方法不需要由我们主动调用，因为由spring容器扫描，并且标注了@KafkaListener，当有消息需要消费时，
     * 就会由Kafka消费掉。
     */
    @Override
    @KafkaListener(topics = {Constant.TOPIC}, groupId = "coupon-hxl")
    public void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        // 如果存在消息的情况
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            CouponKafkaMessage couponInfo = JSON.parseObject(message.toString(), CouponKafkaMessage.class);

            log.info("Receive CouponKafkaMessage: {}", message.toString());

            CouponStatus status = CouponStatus.of(couponInfo.getStatus());

            switch (status) {
                case USABLE:
                    // 其实在这种状态下是不需要kafka的参与的。
                    // 用户只要在领取了以后只有保存在DB中才会有coupon_id的生成，而这种情况是同步进行的，
                    // 不需要kafka异步参与。
                    break;
                case USED:
                    processUsedCoupons(couponInfo, status);
                    break;
                case EXPIRED:
                    processExpiredCoupons(couponInfo, status);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * <h2>处理已使用的用户优惠券</h2>
     * */
    private void processUsedCoupons(CouponKafkaMessage kafkaMessage, CouponStatus status) {
        // TODO 给用户发送短信
        processCouponsByStatus(kafkaMessage, status);
    }

    /**
     * <h2>处理过期的用户优惠券</h2>
     * */
    private void processExpiredCoupons(CouponKafkaMessage kafkaMessage, CouponStatus status) {
        // TODO 给用户发送推送
        processCouponsByStatus(kafkaMessage, status);
    }

    /**
     * <h2>根据状态处理优惠券信息</h2>
     * */
    private void processCouponsByStatus(CouponKafkaMessage kafkaMessage, CouponStatus status) {
        List<Coupon> coupons = couponDao.findAllById(kafkaMessage.getIds());
        if (CollectionUtils.isEmpty(coupons) || coupons.size() != kafkaMessage.getIds().size()) {
            log.error("Can Not Find Right Coupon Info: {}",
                    JSON.toJSONString(kafkaMessage));
            // TODO 发送给负责人邮件
            return;
        }

        coupons.forEach(c -> c.setStatus(status));
        log.info("CouponKafkaMessage Op Coupon Count: {}",
                couponDao.saveAll(coupons).size());
    }
}
