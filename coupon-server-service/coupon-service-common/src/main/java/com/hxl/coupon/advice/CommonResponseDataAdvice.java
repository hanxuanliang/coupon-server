package com.hxl.coupon.advice;

import com.hxl.coupon.annotation.IgnoreResponseAdvice;
import com.hxl.coupon.vo.CommonResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;


/**
 * @Description: 对ResponseBody进行增强
 * @Author: hanxuanliang
 * @Date: 2019/12/15 10:35
 */
@RestControllerAdvice
@SuppressWarnings("all")
public class CommonResponseDataAdvice implements ResponseBodyAdvice<Object> {

    /**
     * @Description: 判断是否对响应进行处理
     * @param: methodParameter： Controller 方法定义
     * @return: boolean 返回true则激活 beforeBodyWrite() 执行；false 则直接跳过
     * @date: 2019/12/15 10:59
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // 当前方法所在的类标识了 @IgnoreResponseAdvice ，则不需要处理
        if (methodParameter.getDeclaringClass().isAnnotationPresent(
                IgnoreResponseAdvice.class)) { return false; }
        // 当前方法标识了 @IgnoreResponseAdvice ，则不需要处理
        if (methodParameter.getMethod().isAnnotationPresent(
                IgnoreResponseAdvice.class)) { return false; }
        // 其他情况就进行处理，执行 beforeBodyWrite()
        return true;
    }

    /**
     * @Description: 响应返回之前做的封装处理
     * @date: 2019/12/15 11:09
     */
    @Override
    public Object beforeBodyWrite(Object o,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {
        CommonResponse<Object> response = new CommonResponse<>(0, "");
        if (null == o) {
            // 如果 o 是 null，response 不需要设置 data；
            return response;
        } else if (o instanceof CommonResponse) {
            // 如果 o 已经是 CommonResponse，则不需要在做处理；
            response = (CommonResponse<Object>) o;
        } else {
            // 否则则说明需要把 response 做为 CommonResponse 的响应对象data中；
            response.setData(o);
        }
        return response;
    }
}
