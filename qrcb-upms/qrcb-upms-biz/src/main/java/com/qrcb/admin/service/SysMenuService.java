package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.dto.MenuTree;
import com.qrcb.admin.api.entity.SysMenu;
import com.qrcb.admin.api.vo.MenuVo;

import java.util.List;
import java.util.Set;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 菜单权限表 Service 接口 <br/>
 */

public interface SysMenuService extends IService<SysMenu> {

    /**
     * 通过角色编号查询URL 权限
     *
     * @param roleId 角色ID
     * @return {@link MenuVo} List
     */
    List<MenuVo> findMenuByRoleId(Integer roleId);

    /**
     * 级联删除菜单
     *
     * @param id 菜单ID
     * @return {@link Boolean}
     */
    Boolean removeMenuById(Integer id);

    /**
     * 更新菜单信息
     *
     * @param sysMenu 菜单信息
     * @return {@link Boolean}
     */
    Boolean updateMenuById(SysMenu sysMenu);

    /**
     * 构建树查询 1. 不是懒加载情况，查询全部 2. 是懒加载，根据parentId 查询 2.1 父节点为空，则查询ID -1
     *
     * @param lazy     是否是懒加载
     * @param parentId 父节点ID
     * @return {@link MenuTree} List
     */
    List<MenuTree> treeMenu(boolean lazy, Integer parentId);

    /**
     * 查询菜单
     *
     * @param voSet    菜单
     * @param type     类型
     * @param parentId 父节点ID
     * @return {@link MenuTree} List
     */
    List<MenuTree> filterMenu(Set<MenuVo> voSet, String type, Integer parentId);

}
