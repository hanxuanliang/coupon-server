package com.hxl.coupon.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * @Description: 针对filterType的post类型设计的Filter抽象类
 * @Author: hanxuanliang
 * @Date: 2019/12/14 23:19
 */
public abstract class AbstractPostZuulFilter extends AbstractZuulFilter{
    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }
}
