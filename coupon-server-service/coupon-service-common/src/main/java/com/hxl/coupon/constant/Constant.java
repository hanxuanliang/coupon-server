package com.hxl.coupon.constant;

/**
 * @Description: 整个项目的所有常量定义在这
 * @Author: hanxuanliang
 * @Date: 2019/12/17 21:05
 */
public class Constant {
    /** Kafka消息的Topic */
    public static final String TOPIC = "user_coupon_op";

    public static final class RedisPredfix {
        /** 优惠券码的key前缀 */
        public static final String COUPON_TEMPLATE = "coupon_template_code_";

        /** 用户当前可用的优惠券key前缀 */
        public static final String USER_COUPON_USABLE = "coupon_user_usable_";

        /** 用户当前已用的优惠券key前缀 */
        public static final String USER_COUPON_USED = "coupon_user_used_";

        /** 用户当前已过期的优惠券key前缀 */
        public static final String USER_COUPON_EXPIRED = "coupon_user_expired_";
    }

}
