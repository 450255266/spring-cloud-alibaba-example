package com.gitee.eample.member.service.biz;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.eample.member.service.dao.MemberIntegralMapper;
import com.gitee.eample.member.service.domain.MemberIntegral;
import org.springframework.stereotype.Service;

@Service
public class IMemberIntegralBizImpl extends ServiceImpl<MemberIntegralMapper, MemberIntegral> implements IMemberIntegralBiz {
}
