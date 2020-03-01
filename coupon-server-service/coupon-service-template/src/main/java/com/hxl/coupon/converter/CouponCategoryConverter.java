package com.hxl.coupon.converter;

import com.hxl.coupon.enums.CouponCategory;

import javax.persistence.AttributeConverter;
import javax.persistence.Convert;

/**
 * @Description: 优惠券分类枚举类属性转换器
 *      第一个参数X: 实体属性的类型
 *      第二个参数Y: 数据库字段类型
 * @Author: hanxuanliang
 * @Date: 2019/12/17 15:45
 */
@Convert
public class CouponCategoryConverter implements AttributeConverter<CouponCategory, String> {
    
    /**
     * @Description: 将实体属性X转换为Y存储在数据库中，插入和更新时的动作
     * @param: 实体属性X
     * @return: 数据库字段Y
     * @date: 2019/12/17 15:47  
     */
    @Override
    public String convertToDatabaseColumn(CouponCategory couponCategory) {
        return couponCategory.getCode();
    }

    /**
     * @Description: 将数据库中的字段Y转换为实体属性X，查询操作时执行的动作
     * @param: 数据库字段Y
     * @return: 实体属性X
     * @date: 2019/12/17 15:48  
     */
    @Override
    public CouponCategory convertToEntityAttribute(String s) {
        return CouponCategory.of(s);
    }
}
