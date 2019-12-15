package com.hxl.coupon.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Description: 平台线枚举，不同平台的优惠券共用一个系统，
 *      可能存在平台间的互相调用（比如：天猫，淘宝）
 * @Author: hanxuanliang
 * @Date: 2019/12/15 15:08
 */
@Getter
@AllArgsConstructor
public enum PlatformLineCategory {
    /**
     * SKYCAT: 天猫 平台
     * SKYDOG: 天狗 平台
     */
    SKYCAT("天猫", 101),
    SKYDOG("天狗", 102);

    private String description;

    private Integer code;

    public static PlatformLineCategory of(Integer code) {
        Objects.requireNonNull(code);
        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists"));
    }
}
