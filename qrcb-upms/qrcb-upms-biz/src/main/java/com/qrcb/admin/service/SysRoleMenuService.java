package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.entity.SysRoleMenu;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 角色菜单表 Service 接口 <br/>
 */

public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 更新角色菜单
     *
     * @param role 角色
     * @param roleId  角色Id
     * @param menuIds 菜单ID拼成的字符串，每个id之间根据逗号分隔
     * @return {@link Boolean}
     */
    Boolean saveRoleMenus(String role, Integer roleId, String menuIds);

}
