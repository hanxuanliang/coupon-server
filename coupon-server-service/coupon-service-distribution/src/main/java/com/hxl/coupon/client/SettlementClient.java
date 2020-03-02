package com.hxl.coupon.client;

import com.hxl.coupon.client.hystrix.SettlementClientHystrix;
import com.hxl.coupon.exception.CouponException;
import com.hxl.coupon.vo.CommonResponse;
import com.hxl.coupon.vo.SettlementInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Description:
 * @Author: hanxuanliang
 * @Date: 2020/3/2 15:27
 */
@FeignClient(value = "eureka-client-coupon-settlement", fallback = SettlementClientHystrix.class)
public interface SettlementClient {

    /**
     * <h2>优惠券规则计算</h2>
     * */
    @RequestMapping(value = "/coupon-settlement/settlement/compute", method = RequestMethod.POST)
    CommonResponse<SettlementInfo> computeRule(@RequestBody SettlementInfo settlement) throws CouponException;
}

