package com.hxl.coupon.service;

import com.hxl.coupon.entity.Coupon;
import com.hxl.coupon.exception.CouponException;
import com.hxl.coupon.vo.AcquireTemplateRequest;
import com.hxl.coupon.vo.CouponTemplatesdk;
import com.hxl.coupon.vo.SettlementInfo;

import java.util.List;

/**
 * @Description: 用户 业务接口
 *      1. 用户三类状态优惠券信息展示服务
 *      2. 查看用户当前可以领取的优惠券模板 - coupon-template 微服务配合实现
 *      3. 用户领取优惠券服务
 *      4. 用户消费优惠券服务 - coupon-settlement 微服务配合实
 * @Author: hanxuanliang
 * @Date: 2020/3/2 10:35
 */
public interface IUserService {

    /**
     * <h2>根据用户 id 和状态 查询优惠券记录</h2>
     * @param userId 用户 id
     * @param status 优惠券状态
     * @return {@link Coupon}s
     * */
    List<Coupon> findCouponsByStatus(Long userId, Integer status)
            throws CouponException;

    /**
     * <h2>根据用户 id 查找当前可以领取的优惠券模板</h2>
     * @param userId 用户 id
     * @return {@link CouponTemplatesdk}s
     * */
    List<CouponTemplatesdk> findAvailableTemplate(Long userId) throws CouponException;

    /**
     * <h2>用户领取优惠券</h2>
     * @param request {@link AcquireTemplateRequest}
     * @return {@link Coupon}
     * */
    Coupon acquireTemplate(AcquireTemplateRequest request) throws CouponException;

    /**
     * <h2>结算(核销)优惠券</h2>
     * @param info {@link SettlementInfo}
     * @return {@link SettlementInfo}
     * */
    SettlementInfo settlement(SettlementInfo info) throws CouponException;
}

