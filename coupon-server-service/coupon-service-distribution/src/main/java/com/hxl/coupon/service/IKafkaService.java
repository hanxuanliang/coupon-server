package com.hxl.coupon.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @Description: kafka 业务接口
 * @Author: hanxuanliang
 * @Date: 2020/3/2 10:33
 */
public interface IKafkaService {

    /**
     * <h2>消费优惠券 Kafka 消息</h2>
     * @param record {@link ConsumerRecord}
     * */
    void consumeCouponKafkaMessage(ConsumerRecord<?, ?> record);
}

