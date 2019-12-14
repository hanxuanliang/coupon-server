package com.hxl.coupon.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;


/**
 * @Description: 抽象Zuul过滤器
 * @Author: hanxuanliang
 * @Date: 2019/12/14 22:48
 */
public abstract class AbstractZuulFilter extends ZuulFilter {

    /**
     * @Description: 用于在过滤器之间传递消息，数据保存在每个请求的 ThreadLocal 中，
     *      实际上扩展了 ConcurrentHashMap 。
     */
    RequestContext context;

    /**
     * @Description: 标识一下请求是否还要往下进行，如果在前面的Filter就已经返回false，
     *      事实上就不需要再往下进行了。
     */
    private final static String NEXT = "next";

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        return (boolean) ctx.getOrDefault(NEXT, true);
    }

    @Override
    public Object run() throws ZuulException {
        context = RequestContext.getCurrentContext();
        return cRun();
    }
    /**
     * 因为这是一个抽象类，我们并不想在run()里面实现过多的细节，
     *      而自定义的过滤器去实现这个cRun()即可
     * @param: null
     * @return: null
     */
    protected abstract Object cRun();

    /**
     * @Description: 定义了一个快速失败方法 fail()
     * @param: 错误码，错误信息
     */
    Object fail(int code, String msg) {
        // 如果失败了，当前就把 NEXT 设置为false，不再往下执行了
        context.set(NEXT, false);
        context.setSendZuulResponse(false);
        context.getResponse().setContentType("text/html;charset=UTF-8");
        context.setResponseStatusCode(code);
        context.setResponseBody(String.format("{\"result\": \"%s!\"}", msg));
        return null;
    }

    /**
     * @Description: 定义了一个成功快捷方法 success()
     * @param: null
     */
    Object success() {
        context.set(NEXT, true);
        return null;
    }
}
