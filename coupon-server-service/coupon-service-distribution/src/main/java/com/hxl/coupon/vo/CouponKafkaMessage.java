package com.hxl.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 将ids集合的优惠券以及他所对应的状态加入到DB中，通过kafka的消费
 * @Author: hanxuanliang
 * @Date: 2020/3/2 14:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponKafkaMessage {

    /** 优惠券状态 */
    private Integer status;

    /** Coupon 主键 */
    private List<Integer> ids;
}
