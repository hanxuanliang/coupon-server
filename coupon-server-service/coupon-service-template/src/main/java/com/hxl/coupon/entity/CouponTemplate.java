package com.hxl.coupon.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.hxl.coupon.constant.CouponCategory;
import com.hxl.coupon.constant.DistributeTarget;
import com.hxl.coupon.constant.PlatformLineCategory;
import com.hxl.coupon.converter.CouponCategoryConverter;
import com.hxl.coupon.converter.DistributeTargetConverter;
import com.hxl.coupon.converter.PlatformLineConverter;
import com.hxl.coupon.converter.TemplateRuleConverter;
import com.hxl.coupon.serialization.CouponTemplateSerialize;
import com.hxl.coupon.vo.TemplateRule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 优惠券模板实体类定义
 *      基础属性 + 规则属性
 * @Author: hanxuanliang
 * @Date: 2019/12/17 14:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "coupon_template")
@JsonSerialize(using = CouponTemplateSerialize.class)
public class CouponTemplate implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @Column(name = "expired", nullable = false)
    private Boolean expired;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "logo", nullable = false)
    private String logo;

    @Column(name = "intro", nullable = false)
    private String description;

    @Column(name = "category", nullable = false)
    @Convert(converter = CouponCategoryConverter.class)
    private CouponCategory couponCategory;

    @Column(name = "product_line", nullable = false)
    @Convert(converter = PlatformLineConverter.class)
    private PlatformLineCategory platformLineCategory;

    @Column(name = "coupon_count", nullable = false)
    private Integer count;

    @Column(name = "create_time", nullable = false)
    @CreatedDate
    private Date createTime;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "template_key", nullable = false)
    private String templateKey;

    @Column(name = "target", nullable = false)
    @Convert(converter = DistributeTargetConverter.class)
    private DistributeTarget distributeTarget;

    @Column(name = "rule", nullable = false)
    @Convert(converter = TemplateRuleConverter.class)
    private TemplateRule templateRule;

    public CouponTemplate(String name, String logo, String description, String couponCategory,
                          Integer platformLineCategory, Integer count, Long userId,
                          Integer distributeTarget, TemplateRule templateRule) {
        this.available = false;
        this.expired = false;
        this.name = name;
        this.logo = logo;
        this.description = description;
        this.couponCategory = CouponCategory.of(couponCategory);
        this.platformLineCategory = PlatformLineCategory.of(platformLineCategory);
        this.count = count;
        this.userId = userId;
        // 优惠券模板唯一编码：4(平台线+类型) + 8(日期：20191217) + id(扩充为4位)
        this.templateKey = platformLineCategory.toString() + couponCategory +
                new SimpleDateFormat("yyyyMMdd").format(new Date());
        this.distributeTarget = DistributeTarget.of(distributeTarget);
        this.templateRule = templateRule;
    }

}
