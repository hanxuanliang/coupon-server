package com.hxl.coupon.dao;

import com.hxl.coupon.constant.CouponStatus;
import com.hxl.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Description: 分发系统 Dao
 * @Author: hanxuanliang
 * @Date: 2020/3/2 10:17
 */
public interface CouponDao extends JpaRepository<Coupon, Integer> {

    /**
     * <h2>根据 userId + 状态寻找优惠券记录</h2>
     * where userId = ... and status = ...
     * */
    List<Coupon> findAllByUserIdAndStatus(Long userId, CouponStatus status);
}
