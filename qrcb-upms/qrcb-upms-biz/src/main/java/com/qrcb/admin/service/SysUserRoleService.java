package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.entity.SysUserRole;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 用户角色表 Service 接口 <br/>
 */

public interface SysUserRoleService extends IService<SysUserRole> {

    /**
     * 根据用户Id删除该用户的角色关系
     *
     * @param userId 用户ID
     * @return {@link Boolean}
     */
    Boolean deleteByUserId(Integer userId);
}
