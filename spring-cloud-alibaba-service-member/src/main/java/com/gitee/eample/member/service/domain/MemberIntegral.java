package com.gitee.eample.member.service.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

@TableName("t_member_integral")
public class MemberIntegral implements Serializable {

    private static final long serialVersionUID = -5809782578272943999L;


    @TableId
    private Long id;
    private String username;
    private int integral;
    private Date credate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public Date getCredate() {
        return credate;
    }

    public void setCredate(Date credate) {
        this.credate = credate;
    }
}
