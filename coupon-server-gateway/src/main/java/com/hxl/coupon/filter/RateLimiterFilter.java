package com.hxl.coupon.filter;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 限流网关
 * @Author: hanxuanliang
 * @Date: 2019/12/14 23:28
 */
@Slf4j
@Component
@SuppressWarnings("all")
public class RateLimiterFilter extends AbstractPreZuulFilter {

    /**
     * @Description: 每秒可以获得两个令牌
     */
    RateLimiter rateLimiter = RateLimiter.create(2.0);

    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        if (rateLimiter.tryAcquire()) {
            log.info("get rate token success");
            return success();
        } else {
            log.error("rate limit: {}", request.getRequestURL());
            return fail(402, "error: rate limit");
        }
    }

    @Override
    public int filterOrder() {
        return 2;
    }
}
