package com.hxl.coupon.executor;

import com.hxl.coupon.constant.RuleFlag;
import com.hxl.coupon.enums.CouponCategory;
import com.hxl.coupon.exception.CouponException;
import com.hxl.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Description:
 * @Author: hanxuanliang
 * @Date: 2020/3/6 11:43
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class ExecuteManager implements BeanPostProcessor {

    /** 规则执行器映射 */
    private static Map<RuleFlag, RuleExecutor> executorIndex = new HashMap<>(RuleFlag.values().length);

    /**
     * <h2>优惠券结算规则计算入口</h2>
     * 注意: 一定要保证传递进来的优惠券个数 >= 1
     * */
    public SettlementInfo computeRule(SettlementInfo settlement) throws CouponException {

        SettlementInfo result = null;

        // 单类优惠券
        if (settlement.getCouponAndTemplateinfos().size() == 1) {

            // 获取优惠券的类别
            CouponCategory category = CouponCategory.of(
                    settlement.getCouponAndTemplateinfos().get(0).getTemplatesdk().getCategory());

            switch (category) {
                case FULL_REDUCTION:
                    result = executorIndex.get(RuleFlag.MANJIAN)
                            .computeRule(settlement);
                    break;
                case DISCOUNT:
                    result = executorIndex.get(RuleFlag.ZHEKOU)
                            .computeRule(settlement);
                    break;
                case IMMEDIATE_DISCOUNT:
                    result = executorIndex.get(RuleFlag.LIJIAN)
                            .computeRule(settlement);
                    break;
            }
        } else {
            // 多类优惠券
            List<CouponCategory> categories = new ArrayList<>(settlement.getCouponAndTemplateinfos().size());

            settlement.getCouponAndTemplateinfos().forEach(ct ->
                    categories.add(CouponCategory.of(ct.getTemplatesdk().getCategory())));
            if (categories.size() != 2) {
                throw new CouponException("Not Support For More Template Category");
            } else {
                if (categories.contains(CouponCategory.FULL_REDUCTION)
                        && categories.contains(CouponCategory.DISCOUNT)) {
                    result = executorIndex.get(RuleFlag.MANJIAN_ZHEKOU).computeRule(settlement);
                } else {
                    throw new CouponException("Not Support For Other Template Category");
                }
            }
        }

        return result;
    }

    /**
     * <h2>在 bean 初始化之前去执行(before)</h2>
     * */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName)
            throws BeansException {

        if (!(bean instanceof RuleExecutor)) {
            return bean;
        }

        RuleExecutor executor = (RuleExecutor) bean;
        RuleFlag ruleFlag = executor.ruleConfig();

        if (executorIndex.containsKey(ruleFlag)) {
            throw new IllegalStateException("There is already an executor" +
                    "for rule flag: " + ruleFlag);
        }

        log.info("Load executor {} for rule flag {}.",
                executor.getClass(), ruleFlag);
        executorIndex.put(ruleFlag, executor);

        return null;
    }

    /**
     * <h2>在 bean 初始化之后去执行(after)</h2>
     * */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }
}