package com.hxl.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.client.RestTemplate;

/**
 * @Description: 分发系统 入口类
 * @Author: hanxuanliang
 * @Date: 2020/3/2 8:34
 */
@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@EnableCircuitBreaker
@EnableJpaAuditing
public class DistributionApplication {

    public static void main(String[] args) { SpringApplication.run(DistributionApplication.class,args); }

    @Bean
    @LoadBalanced  //开启负载均衡
    RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
