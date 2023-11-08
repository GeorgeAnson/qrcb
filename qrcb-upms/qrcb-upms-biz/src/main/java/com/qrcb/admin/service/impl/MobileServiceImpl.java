package com.qrcb.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qrcb.admin.api.entity.SysUser;
import com.qrcb.admin.mapper.SysUserMapper;
import com.qrcb.admin.service.MobileService;
import com.qrcb.common.core.assemble.constant.CacheConstants;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.assemble.constant.enums.LoginTypeEnum;
import com.qrcb.common.core.assemble.exception.CheckedException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 手机登录相关业务实现 <br/>
 */

@Slf4j
@Service
@AllArgsConstructor
public class MobileServiceImpl implements MobileService {

    private final RedisTemplate redisTemplate;

    private final SysUserMapper userMapper;

    @Override
    public String sendSmsCode(String mobile) {
        List<SysUser> userList = userMapper.selectList(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getPhone, mobile));

        if (CollUtil.isEmpty(userList)) {
            log.info("手机号未注册:{}", mobile);
            throw new CheckedException("手机号未注册");
        }

        Object codeObj = redisTemplate.opsForValue()
                .get(CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType() + StrUtil.AT + mobile);

        if (codeObj != null) {
            log.info("手机号验证码未过期:{}，{}", mobile, codeObj);
            throw new CheckedException("验证码发送过频繁");
        }

        String code = RandomUtil.randomNumbers(Integer.parseInt(SecurityConstants.CODE_SIZE));
        log.debug("手机号生成验证码成功:{},{}", mobile, code);
        redisTemplate.opsForValue().set(
                CacheConstants.DEFAULT_CODE_KEY + LoginTypeEnum.SMS.getType() + StrUtil.AT + mobile,
                code, SecurityConstants.CODE_TIME, TimeUnit.SECONDS);
        return code;
    }

}
