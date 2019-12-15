package com.hxl.coupon.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 请求日志过滤器
 * @Author: hanxuanliang
 * @Date: 2019/12/15 9:16
 */
@Slf4j
@Component
public class AccessLogFilter extends AbstractPostZuulFilter {
    @Override
    protected Object cRun() {
        HttpServletRequest request = context.getRequest();
        Long startTime = (Long) context.get("startTime");
        String uri = request.getRequestURI();
        long duration = System.currentTimeMillis() - startTime;
        log.info("uri: {}, duration: {}", uri, duration);
        return success();
    }

    @Override
    public int filterOrder() {
        // 返回所有过滤器之前去执行当前过滤器，所有 -1
        return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
    }
}
