package com.hxl.coupon.serialize;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hxl.coupon.entity.Coupon;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @Description: 优惠券 序列化器
 * @Author: hanxuanliang
 * @Date: 2020/3/2 9:12
 */
public class CouponSerialize extends JsonSerializer<Coupon> {

    @Override
    public void serialize(Coupon coupon, JsonGenerator generator, SerializerProvider serializerProvider) throws IOException {
        // 开始序列化
        generator.writeStartObject();

        generator.writeStringField("id", coupon.getId().toString());
        generator.writeStringField("templateId",
                coupon.getTemplateId().toString());
        generator.writeStringField("userId",
                coupon.getUserId().toString());
        generator.writeStringField("couponCode",
                coupon.getCouponCode());
        generator.writeStringField("assignTime",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(coupon.getAssignTime()));
        generator.writeStringField("name",
                coupon.getTemplatesdk().getName());
        generator.writeStringField("logo",
                coupon.getTemplatesdk().getLogo());
        generator.writeStringField("desc",
                coupon.getTemplatesdk().getDescription());
        generator.writeStringField("expiration",
                JSON.toJSONString(
                        coupon.getTemplatesdk().getRule().getExpiration()));
        generator.writeStringField("discount",
                JSON.toJSONString(
                        coupon.getTemplatesdk().getRule().getDiscount()));
        generator.writeStringField("usage",
                JSON.toJSONString(coupon.getTemplatesdk().getRule().getUsage()));

        // 结束序列化
        generator.writeEndObject();
    }
}
