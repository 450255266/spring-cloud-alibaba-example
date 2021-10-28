package com.gitee.eample.user.service.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gtiee.example.common.exception.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * rest template consumer
 *
 * @author wentao.wu
 */
@RestController
@RequestMapping("/rest/consumer")
public class RestTamplteConsumerController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/getUserInfo/{username}")
    public Response<Map<String, Object>> getUserInfo(@PathVariable("username") String username) {
        // 通过的负载均衡接口获取服务实例信息
        ServiceInstance serviceInstance = loadBalancerClient.choose("service-member");
        String url = "http://" + serviceInstance.getServiceId() + ":" + serviceInstance.getPort() + "/member/info/getUserMember/" + username;
        String result = restTemplate.getForObject(url, String.class);
        Response<Map<String, Object>> response = JSON.toJavaObject(JSONObject.parseObject(result), Response.class);
        Map<String, Object> userinfo = new HashMap<>();
        userinfo.put("userage", "100");
        userinfo.put("email", "xxx@email.com");
        response.getResult().putAll(userinfo);
        response.setMsg("获取用户信息成功!");
        return response;
    }

}
