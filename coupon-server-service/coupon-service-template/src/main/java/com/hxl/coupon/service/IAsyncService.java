package com.hxl.coupon.service;

import com.hxl.coupon.entity.CouponTemplate;

/**
 * @Description: 异步服务接口定义
 * @Author: hanxuanliang
 * @Date: 2019/12/17 19:52
 */
public interface IAsyncService {
    /**
     * 根据模板异步创建优惠券码
     * @param template 优惠券模板实体
     */
    void asyncContructCouponTemplate(CouponTemplate template);
}
