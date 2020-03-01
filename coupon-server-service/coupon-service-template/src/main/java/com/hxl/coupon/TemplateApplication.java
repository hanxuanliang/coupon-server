package com.hxl.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @Description: 模板微服务入口类
 * @Author: hanxuanliang
 * @Date: 2019/12/15 14:39
 */
@EnableScheduling
// 对某些列进行自动赋值
@EnableJpaAuditing
@EnableEurekaClient
@SpringBootApplication
public class TemplateApplication {
    public static void main(String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }
}
