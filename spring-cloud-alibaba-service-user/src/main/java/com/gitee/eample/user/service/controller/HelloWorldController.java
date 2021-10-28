package com.gitee.eample.user.service.controller;

import com.gtiee.example.common.exception.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HelloWorld Web
 *
 * @author wentao.wu
 */
@RequestMapping(value = "/hello/")
@RestController
public class HelloWorldController {
    @GetMapping("/say/{name}")
    public Response<String> say(@PathVariable("name") String name) {
        Response<String> response = new Response<>();
        response.setCode("1");
        response.setMsg("你好啊," + name + "!");
        return response;
    }
}
