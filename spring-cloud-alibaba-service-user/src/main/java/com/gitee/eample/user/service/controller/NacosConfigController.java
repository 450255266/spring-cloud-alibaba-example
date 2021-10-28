package com.gitee.eample.user.service.controller;

import com.gtiee.example.common.exception.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Nacos Config
 *
 * @author wentao.wu
 */
@RequestMapping(value = "/nacos/config/")
@RestController
@RefreshScope
public class NacosConfigController {
    @Value("${nacos.config.msg}")
    private String msg;
    @GetMapping("/getMsg")
    public Response<String> getMsg() {
        Response<String> response = new Response<>();
        response.setCode("1");
        response.setMsg(msg);
        return response;
    }
}
