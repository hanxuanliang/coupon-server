package com.hxl.coupon.executor.impl;

import com.hxl.coupon.constant.RuleFlag;
import com.hxl.coupon.executor.AbstractExecutor;
import com.hxl.coupon.executor.RuleExecutor;
import com.hxl.coupon.vo.CouponTemplatesdk;
import com.hxl.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @Description:
 * @Author: hanxuanliang
 * @Date: 2020/3/6 13:40
 */
@Slf4j
@Component
public class ManJianExecutor extends AbstractExecutor implements RuleExecutor {

    /**
     * <h2>规则类型标记</h2>
     * @return {@link RuleFlag}
     */
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.MANJIAN;
    }

    /**
     * <h2>优惠券规则的计算</h2>
     * @param settlement {@link SettlementInfo} 包含了选择的优惠券
     * @return {@link SettlementInfo} 修正过的结算信息
     */
    @Override
    public SettlementInfo computeRule(SettlementInfo settlement) {

        double goodsSum = retain2Decimals(goodsCostSum(settlement.getGoodsInfos()));
        SettlementInfo probability = processGoodsTypeNotSatisfy(settlement, goodsSum);
        if (null != probability) {
            log.debug("ManJian Template Is Not Match To GoodsType!");
            return probability;
        }

        // 判断满减是否符合折扣标准
        CouponTemplatesdk templatesdk = settlement.getCouponAndTemplateinfos().get(0).getTemplatesdk();
        double base = (double) templatesdk.getRule().getDiscount().getBase();
        double quota = (double) templatesdk.getRule().getDiscount().getQuota();

        // 如果不符合标准, 则直接返回商品总价
        if (goodsSum < base) {
            log.debug("Current Goods Cost Sum < ManJian Coupon Base!");
            settlement.setCost(goodsSum);
            settlement.setCouponAndTemplateinfos(Collections.emptyList());
            return settlement;
        }

        // 计算使用优惠券之后的价格 - 结算
        settlement.setCost(retain2Decimals(Math.max((goodsSum - quota), minCost())));
        log.debug("Use ManJian Coupon Make Goods Cost From {} To {}",
                goodsSum, settlement.getCost());

        return settlement;
    }
}

