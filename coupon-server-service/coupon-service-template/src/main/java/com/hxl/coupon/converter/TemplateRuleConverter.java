package com.hxl.coupon.converter;

import com.alibaba.fastjson.JSON;
import com.hxl.coupon.vo.TemplateRule;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * @Description: 模板规则属性转换器
 * @Author: hanxuanliang
 * @Date: 2019/12/17 15:59
 */
@Convert
public class TemplateRuleConverter implements AttributeConverter<TemplateRule, String> {
    @Override
    public String convertToDatabaseColumn(TemplateRule templateRule) {
        return JSON.toJSONString(templateRule);
    }

    @Override
    public TemplateRule convertToEntityAttribute(String s) {
        return JSON.parseObject(s, TemplateRule.class);
    }
}
