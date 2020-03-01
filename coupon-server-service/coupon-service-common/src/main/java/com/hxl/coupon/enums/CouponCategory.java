package com.hxl.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Description: 优惠券枚举分类
 * @Author: hanxuanliang
 * @Date: 2019/12/15 14:56
 */
@Getter
@AllArgsConstructor
public enum CouponCategory {
    /**
     * full_reduction: 满减券
     * discount: 折扣券
     * immediate_discount: 立减券
     */
    FULL_REDUCTION("满减券", "2001"),
    DISCOUNT("折扣券", "2002"),
    IMMEDIATE_DISCOUNT("立减券", "2003");

    private String description;

    private String code;

    public static CouponCategory of(String code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists"));
    }
}
