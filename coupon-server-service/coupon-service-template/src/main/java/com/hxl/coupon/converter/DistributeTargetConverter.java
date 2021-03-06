package com.hxl.coupon.converter;

import com.hxl.coupon.enums.DistributeTarget;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @Description: 优惠券分发目标枚举属性转换器
 * @Author: hanxuanliang
 * @Date: 2019/12/17 15:55
 */
@Converter
public class DistributeTargetConverter implements AttributeConverter<DistributeTarget, Integer> {
    @Override
    public Integer convertToDatabaseColumn(DistributeTarget distributeTarget) {
        return distributeTarget.getCode();
    }

    @Override
    public DistributeTarget convertToEntityAttribute(Integer integer) {
        return DistributeTarget.of(integer);
    }
}
