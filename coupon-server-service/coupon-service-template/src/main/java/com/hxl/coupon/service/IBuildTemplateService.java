package com.hxl.coupon.service;

import com.hxl.coupon.entity.CouponTemplate;
import com.hxl.coupon.exception.CouponException;
import com.hxl.coupon.vo.TemplateRequest;

/**
 * @Description: 构建优惠券模板接口定义
 * @Author: hanxuanliang
 * @Date: 2019/12/17 19:25
 */
public interface IBuildTemplateService {
    /**
     * 构建模板
     * @param request 模板信息请求对象 {@link TemplateRequest}
     * @return CouponTemplate  {@link CouponTemplate}
     * @throws CouponException 自定义错误 {@link CouponException}
     */
    CouponTemplate buildTemplate(TemplateRequest request) throws CouponException;
}
