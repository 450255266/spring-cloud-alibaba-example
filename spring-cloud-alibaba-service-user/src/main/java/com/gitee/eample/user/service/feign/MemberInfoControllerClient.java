package com.gitee.eample.user.service.feign;

import com.gtiee.example.common.exception.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * service-member服务远程调用接口
 *
 * @author wentao.wu
 */
@FeignClient(name = "service-member")
public interface MemberInfoControllerClient {
    /**
     * 通过用户名称获取会员信息
     *
     * @param username
     * @return
     */
    @GetMapping("/member/info/getUserMember/{username}")
    Response<Map<String, Object>> getUserMember(@PathVariable("username") String username);

    /**
     * 登录送积分
     *
     * @param username
     * @return
     */
    @PostMapping("/member/integral/login/{username}")
    Response<Boolean> login(@PathVariable("username")String username);
}
