package com.hxl.coupon.converter;

import com.hxl.coupon.constant.PlatformLineCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * @Description: 平台线枚举属性转换器
 * @Author: hanxuanliang
 * @Date: 2019/12/17 15:52
 */
@Convert
public class PlatformLineConverter implements AttributeConverter<PlatformLineCategory, Integer> {
    @Override
    public Integer convertToDatabaseColumn(PlatformLineCategory platformLineCategory) {
        return platformLineCategory.getCode();
    }

    @Override
    public PlatformLineCategory convertToEntityAttribute(Integer integer) {
        return PlatformLineCategory.of(integer);
    }
}
