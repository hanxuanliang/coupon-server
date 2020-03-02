package com.hxl.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: hanxuanliang
 * @Date: 2020/3/2 10:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcquireTemplateRequest {

    /** 用户 id */
    private Long userId;

    /** 优惠券模板信息 */
    private CouponTemplatesdk templatesdk;
}

