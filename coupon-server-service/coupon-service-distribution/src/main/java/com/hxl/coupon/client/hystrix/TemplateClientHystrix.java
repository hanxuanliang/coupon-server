package com.hxl.coupon.client.hystrix;

import com.hxl.coupon.client.TemplateClient;
import com.hxl.coupon.vo.CommonResponse;
import com.hxl.coupon.vo.CouponTemplatesdk;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Description:
 * @Author: hanxuanliang
 * @Date: 2020/3/2 15:27
 */
@Slf4j
@Component
public class TemplateClientHystrix implements TemplateClient {

    /**
     * <h2>查找所有可用的优惠券模板</h2>
     */
    @Override
    public CommonResponse<List<CouponTemplatesdk>> findAllUsableTemplate() {

        log.error("[eureka-client-coupon-template] findAllUsableTemplate " + "request error");
        return new CommonResponse<>(-1, "[eureka-client-coupon-template] request error",
                Collections.emptyList()
        );
    }

    /**
     * <h2>获取模板 ids 到 CouponTemplateSDK 的映射</h2>
     * @param ids 优惠券模板 id
     */
    @Override
    public CommonResponse<Map<Integer, CouponTemplatesdk>> findids2Templatesdk(Collection<Integer> ids) {

        log.error("[eureka-client-coupon-template] findIds2TemplateSDK" + "request error");

        return new CommonResponse<>(-1, "[eureka-client-coupon-template] request error",
                new HashMap<>(4)
        );
    }
}

