package com.gitee.eample.user.service.controller.command;

public class UserLoginCommand {
    /**
     * 用户名称
     */
    private String username;
    /**
     * 用户密码
     */
    private String pwd;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
