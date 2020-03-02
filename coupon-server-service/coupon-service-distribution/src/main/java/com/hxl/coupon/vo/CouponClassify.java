package com.hxl.coupon.vo;

import com.alibaba.fastjson.JSON;
import com.hxl.coupon.constant.CouponStatus;
import com.hxl.coupon.entity.Coupon;
import com.hxl.coupon.enums.PeriodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 用户优惠券的分类, 根据优惠券状态
 * @Author: hanxuanliang
 * @Date: 2020/3/2 15:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class CouponClassify {

    /** 可以使用的 */
    private List<Coupon> usable;

    /** 已使用的 */
    private List<Coupon> used;

    /** 已过期的 */
    private List<Coupon> expired;

    /**
     * <h2>对当前的优惠券进行分类</h2>
     * */
    public static CouponClassify classify(List<Coupon> coupons) {
        log.debug("classify传入参数 ==> {}", JSON.toJSONString(coupons));

        List<Coupon> usable = new ArrayList<>(coupons.size());
        List<Coupon> used = new ArrayList<>(coupons.size());
        List<Coupon> expired = new ArrayList<>(coupons.size());

        coupons.forEach(c -> {
            // 判断优惠券是否过期
            boolean isTimeExpire = false;
            long curTime = System.currentTimeMillis();
            // 如果是固定日期过期
            if (c.getTemplatesdk().getRule().getExpiration().getPeriod().equals(PeriodType.REGULAR_PERIOD.getCode())){
                isTimeExpire = c.getTemplatesdk().getRule().getExpiration().getDeadline() <= curTime;
            } else {
                // 与用户领取时间有关【用户领取时间：c.getAssignTime()；变动时间：getGap()】
                isTimeExpire = DateUtils.addDays(
                        c.getAssignTime(), c.getTemplatesdk().getRule().getExpiration().getGap()
                ).getTime() <= curTime;
            }

            if (c.getStatus().equals(CouponStatus.USED)){
                used.add(c);
            } else if(c.getStatus().equals(CouponStatus.EXPIRED) || isTimeExpire){
                expired.add(c);
            } else {
                usable.add(c);
            }
        });
        return new CouponClassify(usable, used, expired);
    }
}
