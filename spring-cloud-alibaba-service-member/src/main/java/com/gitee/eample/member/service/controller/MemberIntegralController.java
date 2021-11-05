package com.gitee.eample.member.service.controller;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.gitee.eample.member.service.biz.IMemberIntegralBiz;
import com.gitee.eample.member.service.domain.MemberIntegral;
import com.gtiee.example.common.exception.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 用户积分
 *
 * @author wentao.wu
 */
@RestController
@RequestMapping("/member/integral")
public class MemberIntegralController {
    @Autowired
    private IMemberIntegralBiz memberIntegralBiz;

    @PostMapping("/login/{username}")
    public Response<Boolean> login(@PathVariable("username") String username) {
        // 第一次登录则增加积分，我这里就不判断了，每次调用都新增一条积分记录了
        MemberIntegral memberIntegral = new MemberIntegral();
        memberIntegral.setId(IdWorker.getId());
        memberIntegral.setIntegral(10);
        memberIntegral.setUsername(username);
        memberIntegral.setCredate(new Date());
        memberIntegralBiz.save(memberIntegral);
        return Response.createOk("登录新增会员积分成功!", true);
    }
}
