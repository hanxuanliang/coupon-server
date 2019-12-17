package com.hxl.coupon.serialization;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hxl.coupon.entity.CouponTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @Description: 优惠券实体类序列化器
 * @Author: hanxuanliang
 * @Date: 2019/12/17 16:04
 */
public class CouponTemplateSerialize extends JsonSerializer<CouponTemplate> {

    @Override
    public void serialize(CouponTemplate couponTemplate,
                          JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("id", couponTemplate.getId().toString());
        jsonGenerator.writeStringField("name", couponTemplate.getName());
        jsonGenerator.writeStringField("logo", couponTemplate.getLogo());
        jsonGenerator.writeStringField("description", couponTemplate.getDescription());
        jsonGenerator.writeStringField("couponCategory", couponTemplate.getCouponCategory().getDescription());
        jsonGenerator.writeStringField("platformLineCategory", couponTemplate.getPlatformLineCategory().getDescription());
        jsonGenerator.writeStringField("count", couponTemplate.getCount().toString());
        jsonGenerator.writeStringField("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(couponTemplate.getCreateTime()));
        jsonGenerator.writeStringField("userId", couponTemplate.getUserId().toString());
        jsonGenerator.writeStringField("key", couponTemplate.getTemplateKey() + String.format("%04d", couponTemplate.getId()));
        jsonGenerator.writeStringField("distributeTarget", couponTemplate.getDistributeTarget().getDescription());
        jsonGenerator.writeStringField("rule", JSON.toJSONString(couponTemplate.getTemplateRule()));

        jsonGenerator.writeEndObject();
    }
}
