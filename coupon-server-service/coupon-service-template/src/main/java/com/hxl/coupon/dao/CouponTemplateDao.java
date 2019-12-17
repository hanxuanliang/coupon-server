package com.hxl.coupon.dao;

import com.hxl.coupon.entity.CouponTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Description: 优惠券模板Dao层接口定义
 * @Author: hanxuanliang
 * @Date: 2019/12/17 16:26
 */
public interface CouponTemplateDao extends JpaRepository<CouponTemplate, Integer> {

    /**
     * 根据模板名称去查询
     * @param name 模板名字
     * @return CouponTemplate
     */
    CouponTemplate findByName(String name);

    /**
     * 根据 available 和 expired 标记查找模板记录
     * @param available 是否可用
     * @param expired 是否过期
     * @return List<CouponTemplate>
     */
    List<CouponTemplate> findAllByAvailableAndExpired(Boolean available, Boolean expired);

    /**
     * 根据 expired 标记查找模板记录
     * @param expired 是否过期
     * @return List<CouponTemplate>
     */
    List<CouponTemplate> findAllByExpired(Boolean expired);
}
