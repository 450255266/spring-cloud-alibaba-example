package com.gitee.eample.member.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RestController;

/**
 * 会员中心服务启动类
 *
 * @author wentao.wu
 */
@MapperScan("com.gitee.eample.member.service.dao")
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class MemberServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MemberServiceApplication.class, args);
    }
}
