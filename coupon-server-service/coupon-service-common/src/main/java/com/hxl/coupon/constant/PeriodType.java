package com.hxl.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Description: 优惠券有效期类型枚举
 * @Author: hanxuanliang
 * @Date: 2019/12/15 15:39
 */
@Getter
@AllArgsConstructor
public enum PeriodType {

    /**
     * REGULAR_PERIOD: 给定出准确固定的日期期限
     * SHIFT_PERIOD: 以领取之日开始计算日期期限
     */
    REGULAR_PERIOD("固定的日期期限", 2010),
    SHIFT_PERIOD("变动(以领取之日开始计算日期期限)", 2011);

    private String description;
    private Integer code;

    public static PeriodType of(Integer code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists"));
    }
}
