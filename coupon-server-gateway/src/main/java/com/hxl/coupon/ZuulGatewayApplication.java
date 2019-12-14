package com.hxl.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @Description: 网关应用启动入口
 * @Author: hanxuanliang
 * @Date: 2019/12/14 22:34
 * 1. @EnableZuulProxy 标识当前应用为 Zuul Server；
 * 2. @SpringCloudApplication 组合 Springboot + 服务发现 + 熔断
 */
@EnableZuulProxy
@SpringCloudApplication
public class ZuulGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZuulGatewayApplication.class, args);
    }
}
