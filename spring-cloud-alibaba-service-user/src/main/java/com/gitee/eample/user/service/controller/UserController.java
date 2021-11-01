package com.gitee.eample.user.service.controller;

import com.gitee.eample.user.service.biz.IUserBiz;
import com.gitee.eample.user.service.controller.command.UserLoginCommand;
import com.gtiee.example.common.exception.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * User Business Controller
 *
 * @author wentao.wu
 */
@RestController
@RequestMapping("/users/")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUserBiz userBiz;

    @PostMapping("/login")
    public Response<Boolean> login(UserLoginCommand command) {
        try {
            boolean result = userBiz.login(command);
            if (result) {
                return Response.createOk("登录并赠送积分成功!", result);
            }else{
                return Response.createError("账号或密码不存在!", result);
            }
        } catch (Exception e) {
            logger.error("登录失败!", e);
            return Response.createError("服务器繁忙请稍后再试!", false);
        }
    }

}
