package com.hxl.coupon.client.hystrix;

import com.hxl.coupon.client.SettlementClient;
import com.hxl.coupon.exception.CouponException;
import com.hxl.coupon.vo.CommonResponse;
import com.hxl.coupon.vo.SettlementInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author: hanxuanliang
 * @Date: 2020/3/2 15:27
 */
@Slf4j
@Component
public class SettlementClientHystrix implements SettlementClient {

    /**
     * <h2>优惠券规则计算</h2>
     * @param settlement {@link SettlementInfo}
     */
    @Override
    public CommonResponse<SettlementInfo> computeRule(SettlementInfo settlement) throws CouponException {
        log.error("[eureka-client-coupon-settlement] computeRule" + "request error");

        settlement.setEmploy(false);
        settlement.setCost(-1.0);

        return new CommonResponse<>(-1, "[eureka-client-coupon-settlement] request error",
                settlement
        );
    }
}

