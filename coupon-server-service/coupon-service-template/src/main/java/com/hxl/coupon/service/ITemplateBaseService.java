package com.hxl.coupon.service;

import com.hxl.coupon.entity.CouponTemplate;
import com.hxl.coupon.exception.CouponException;
import com.hxl.coupon.vo.CouponTemplatesdk;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description: 优惠券模板基础服务(view delete update...)
 * @Author: hanxuanliang
 * @Date: 2019/12/17 20:07
 */
public interface ITemplateBaseService {
    /**
     * 根据优惠券模板 id 获取优惠券模板
     * @param id 优惠券模板id
     * @return CouponTemplate
     * @throws CouponException 自定义优惠券异常
     */
    CouponTemplate buildTemplateInfo(Integer id) throws CouponException;

    /**
     * 查找所有可用的优惠劵模板
     * @return CouponTemplate
     */
    List<CouponTemplatesdk> findAllUsableTemplate();

    /**
     * 获取模板 ids 到 CouponTemplatesdk 的映射
     * @param ids 多个模板id
     * @return Map<Integer, CouponTemplatesdk> 映射
     */
    Map<Integer, CouponTemplatesdk> findIdsToTemplatesdk(Collection<Integer> ids);
}
