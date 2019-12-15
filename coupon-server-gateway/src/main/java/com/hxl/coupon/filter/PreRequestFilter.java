package com.hxl.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Description: 在过滤器中存储客户端发起请求的时间戳
 * @Author: hanxuanliang
 * @Date: 2019/12/15 9:14
 */
@Slf4j
@Component
public class PreRequestFilter extends AbstractPreZuulFilter {
    @Override
    protected Object cRun() {
        context.set("startTime", System.currentTimeMillis());
        return null;
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
