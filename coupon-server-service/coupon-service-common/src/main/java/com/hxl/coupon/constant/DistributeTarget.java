package com.hxl.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Description: 优惠券分发目标
 * @Author: hanxuanliang
 * @Date: 2019/12/15 15:17
 */
@Getter
@AllArgsConstructor
public enum DistributeTarget {
    /**
     * SINGLE: 单用户领取
     * MULTI: 批量领取
     */
    SINGLE("单用户", 601),
    MULTI("多用户", 602);

    private String description;

    private Integer code;

    public static DistributeTarget of(Integer code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists"));
    }
}
