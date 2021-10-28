package com.gitee.eample.user.service.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTample配置
 */
@Configuration
public class RestTemplateConfig {
    /**
     * 注入RestTample模板并且开启负载均衡
     */
    @Bean
    @LoadBalanced
    public RestTemplate getRestTample(){
        return new RestTemplate();
    }
}
