package com.gitee.eample.user.service.biz;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.eample.user.service.controller.command.UserLoginCommand;
import com.gitee.eample.user.service.domain.User;

public interface IUserBiz extends IService<User> {
    /**
     * 用户登录并且赠送第一次登录积分
     *
     * @param command
     * @return
     */
    boolean login(UserLoginCommand command);

}
