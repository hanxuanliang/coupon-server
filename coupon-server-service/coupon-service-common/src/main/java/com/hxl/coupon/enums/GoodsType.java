package com.hxl.coupon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @Description: 商品种类
 * @Author: hanxuanliang
 * @Date: 2020/3/2 10:44
 */
@Getter
@AllArgsConstructor
public enum GoodsType {

    WENYU("文娱", 801),
    SHENGXIAN("生鲜", 802),
    JIAJU("家居", 803),
    OTHERS("其他", 804),
    ALL("全品类", 805);

    private String description;

    private Integer code;

    public static GoodsType of(Integer code) {
        Objects.requireNonNull(code);

        return Stream.of(values())
                .filter(bean -> bean.code.equals(code))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(code + " not exists!"));
    }
}
