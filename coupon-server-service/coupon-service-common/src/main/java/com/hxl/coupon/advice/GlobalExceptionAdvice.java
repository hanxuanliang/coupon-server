package com.hxl.coupon.advice;

import com.hxl.coupon.exception.CouponException;
import com.hxl.coupon.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 全局异常处理
 * @Author: hanxuanliang
 * @Date: 2019/12/15 11:20
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * @Description: 对 CouponException类的统一异常处理
     * @param:
     */
    @ExceptionHandler(value = CouponException.class)
    public CommonResponse<String> handlerCouponException(HttpServletRequest request, CouponException e) {
        CommonResponse<String> response = new CommonResponse<>(-1, "business error");
        response.setData(e.getMessage());
        return response;
    }
}
