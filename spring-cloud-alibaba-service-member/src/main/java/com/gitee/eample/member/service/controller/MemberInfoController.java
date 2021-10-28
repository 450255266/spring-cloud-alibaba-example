package com.gitee.eample.member.service.controller;

import com.gtiee.example.common.exception.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 会员信息接口
 *
 * @author wentao.wu
 */
@RestController
@RequestMapping("/member/info")
public class MemberInfoController {
    /**
     * 获取用户会员信息
     *
     * @param username
     * @return
     */
    @GetMapping("/getUserMember/{username}")
    public Response<Map<String, Object>> getUserMember(@PathVariable("username") String username) {
        Response<Map<String, Object>> response = new Response<>();
        response.setCode("1");
        response.setMsg("获取会员信息成功!");

        //从数据库根据用户查询会员信息
        Map<String, Object> result = new HashMap<>();
        result.put("level", "vip1");
        result.put("username", username);
        response.setResult(result);
        return response;
    }
}
