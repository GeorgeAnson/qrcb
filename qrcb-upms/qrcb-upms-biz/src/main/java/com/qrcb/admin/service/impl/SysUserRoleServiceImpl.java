package com.qrcb.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qrcb.admin.api.entity.SysUserRole;
import com.qrcb.admin.mapper.SysUserRoleMapper;
import com.qrcb.admin.service.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 用户角色表 Service 实现类 <br/>
 */

@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Override
    public Boolean deleteByUserId(Integer userId) {
        return baseMapper.deleteByUserId(userId);
    }

}