package com.gitee.eample.user.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 用户中心服务启动类
 *
 * @author wentao.wu
 */
@MapperScan("com.gitee.eample.user.service.dao")
@EnableDiscoveryClient//服务注册
@EnableFeignClients//服务发现
@SpringBootApplication
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
