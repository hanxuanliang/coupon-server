package com.hxl.coupon.client;

import com.hxl.coupon.client.hystrix.TemplateClientHystrix;
import com.hxl.coupon.vo.CommonResponse;
import com.hxl.coupon.vo.CouponTemplatesdk;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: hanxuanliang
 * @Date: 2020/3/2 15:27
 */
@FeignClient(value = "eureka-client-coupon-template", fallback = TemplateClientHystrix.class)
public interface TemplateClient {

    /**
     * <h2>查找所有可用的优惠券模板</h2>
     * */
    @RequestMapping(value = "/coupon-template/template/sdk/all", method = RequestMethod.GET)
    CommonResponse<List<CouponTemplatesdk>> findAllUsableTemplate();

    /**
     * <h2>获取模板 ids 到 CouponTemplateSDK 的映射</h2>
     * */
    @RequestMapping(value = "/coupon-template/template/sdk/infos", method = RequestMethod.GET)
    CommonResponse<Map<Integer, CouponTemplatesdk>> findids2Templatesdk(
            @RequestParam("ids") Collection<Integer> ids
    );
}
