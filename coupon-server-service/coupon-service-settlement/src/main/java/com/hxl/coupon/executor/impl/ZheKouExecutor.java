package com.hxl.coupon.executor.impl;

import com.hxl.coupon.constant.RuleFlag;
import com.hxl.coupon.executor.AbstractExecutor;
import com.hxl.coupon.executor.RuleExecutor;
import com.hxl.coupon.vo.CouponTemplatesdk;
import com.hxl.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: hanxuanliang
 * @Date: 2020/3/6 15:30
 */
@Slf4j
@Component
public class ZheKouExecutor extends AbstractExecutor implements RuleExecutor {

    /**
     * <h2>规则类型标记</h2>
     * @return {@link RuleFlag}
     */
    @Override
    public RuleFlag ruleConfig() {
        return RuleFlag.ZHEKOU;
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
            log.debug("ZheKou Template Is Not Match GoodsType!");
            return probability;
        }

        // 折扣优惠券可以直接使用, 没有门槛
        CouponTemplatesdk templatesdk = settlement.getCouponAndTemplateinfos()
                .get(0).getTemplatesdk();
        double quota = (double) templatesdk.getRule().getDiscount().getQuota();

        // 计算使用优惠券之后的价格
        settlement.setCost(Math.max(retain2Decimals((goodsSum * (quota * 1.0 / 100))), minCost()));
        log.debug("Use ZheKou Coupon Make Goods Cost From {} To {}",
                goodsSum, settlement.getCost());

        return settlement;
    }
}

