package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.entity.SysRole;
import com.qrcb.admin.api.vo.RoleVo;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 角色表 Service 接口 <br/>
 */

public interface SysRoleService extends IService<SysRole> {

    /**
     * 通过用户ID，查询角色信息
     *
     * @param userId 用户ID
     * @return  {@link SysRole} List
     */
    List<SysRole> findRolesByUserId(Integer userId);

    /**
     * 根据角色ID 查询角色列表
     *
     * @param roleIdList 角色ID列表
     * @param key        缓存key
     * @return {@link SysRole} List
     */
    List<SysRole> findRolesByRoleIds(List<Integer> roleIdList, String key);

    /**
     * 通过角色ID，删除角色
     *
     * @param id 角色ID
     * @return {@link Boolean}
     */
    Boolean removeRoleById(Integer id);

    /**
     * 根据角色菜单列表
     *
     * @param roleVo 角色&菜单列表
     * @return {@link Boolean}
     */
    Boolean updateRoleMenus(RoleVo roleVo);
}
