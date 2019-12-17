package com.hxl.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: 微服务之间用的优惠券模板信息定义，微服务之间的调用，返回给前端的vo对象
 * @Author: hanxuanliang
 * @Date: 2019/12/17 19:56
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponTemplatesdk {
    private Integer id;
    private String name;
    private String logo;
    private String description;
    private String category;
    private Integer platformLine;
    private String key;
    private Integer target;
    private TemplateRule rule;
}
