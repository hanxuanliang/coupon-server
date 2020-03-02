package com.hxl.coupon.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: 结算信息对象定义
 * @Author: hanxuanliang
 * @Date: 2020/3/2 10:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SettlementInfo {

    private Long userId;
    /* 商品列表 */
    private List<GoodsInfo> goodsInfos;
    /* 优惠劵列表 */
    private List<CouponAndTemplateInfo> couponAndTemplateinfos;

    /* 是否使结算生效，即核销  True为核销；False为结算【其实也就是用不用，用了就是核算并销毁；没用就是仅仅只是计算】*/
    private Boolean employ;

    /* 折扣后的价格 */
    private Double cost;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    /* 优惠劵列表 */
    public static class CouponAndTemplateInfo{
        /** coupon id*/
        private Integer id;

        /** CouponTemplateSDK */
        private CouponTemplatesdk templatesdk;
    }
}
