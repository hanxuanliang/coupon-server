package com.hxl.coupon.executor;

import com.hxl.coupon.constant.RuleFlag;
import com.hxl.coupon.vo.SettlementInfo;

/**
 * @Description:
 * @Author: hanxuanliang
 * @Date: 2020/3/6 11:44
 */
public interface RuleExecutor {

    /**
     * <h2>规则类型标记</h2>
     * @return {@link RuleFlag}
     * */
    RuleFlag ruleConfig();

    /**
     * <h2>优惠券规则的计算</h2>
     * @param settlement {@link SettlementInfo} 包含了选择的优惠券
     * @return {@link SettlementInfo} 修正过的结算信息
     * */
    SettlementInfo computeRule(SettlementInfo settlement);
}

