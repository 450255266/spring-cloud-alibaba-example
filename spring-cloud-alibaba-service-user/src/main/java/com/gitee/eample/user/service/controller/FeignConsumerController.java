package com.gitee.eample.user.service.controller;

import com.gitee.eample.user.service.feign.MemberInfoControllerClient;
import com.gtiee.example.common.exception.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Feign template consumer
 *
 * @author wentao.wu
 */
@RestController
@RequestMapping("/feign/consumer")
public class FeignConsumerController {
    @Autowired
    private MemberInfoControllerClient client;

    @GetMapping("/getUserInfo/{username}")
    public Response<Map<String, Object>> getUserInfo(@PathVariable("username") String username) {
        Response<Map<String, Object>> response = client.getUserMember(username);
        Map<String, Object> userinfo = new HashMap<>();
        userinfo.put("userage", "100");
        userinfo.put("email", "xxx@email.com");
        response.getResult().putAll(userinfo);
        response.setMsg("获取用户信息成功!");
        return response;
    }
}
