package com.gitee.eample.user.service.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.eample.user.service.controller.command.UserLoginCommand;
import com.gitee.eample.user.service.dao.UserMapper;
import com.gitee.eample.user.service.domain.User;
import com.gitee.eample.user.service.feign.MemberInfoControllerClient;
import com.gtiee.example.common.exception.Response;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Service
public class IUserBizImpl extends ServiceImpl<UserMapper, User> implements IUserBiz {

    @Autowired
    private MemberInfoControllerClient client;

    @GlobalTransactional(name = "login_add_member_intergral",rollbackFor = Exception.class)//开启分布式事务
    @Override
    public boolean login(UserLoginCommand command) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, command.getUsername())
                .eq(User::getPwd, command.getPwd());
        User loginUser = getOne(wrapper);
        if (ObjectUtils.isEmpty(loginUser)) {
            return false;
        }
        //调用会员登录接口增加积分
        Response<Boolean> response = client.login(command.getUsername());
        if (response.isOk()) {//增加积分成功，或已增加积分
            //调用积分接口成功，修改当前用户登录时间
            loginUser.setLastLoginDate(new Date());
            updateById(loginUser);


            //假设此处发生异常，不但修改当前用户登录时间需要回滚并且新增的会员积分信息也回滚才算正常
            int i = 0 / 0;
            return true;
        } else {
            //增加积分失败
            return false;
        }
    }
}
