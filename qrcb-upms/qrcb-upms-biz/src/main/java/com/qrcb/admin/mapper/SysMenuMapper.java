package com.qrcb.admin.mapper;

import com.qrcb.admin.api.entity.SysMenu;
import com.qrcb.admin.api.vo.MenuVo;
import com.qrcb.common.core.data.datascope.QrcbBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 菜单权限表 Mapper 接口 <br/>
 */

@Mapper
public interface SysMenuMapper extends QrcbBaseMapper<SysMenu> {

    /**
     * 通过角色编号查询菜单
     *
     * @param roleId 角色ID
     * @return {@link MenuVo} List
     */
    List<MenuVo> listMenusByRoleId(Integer roleId);

    /**
     * 通过角色ID查询权限
     *
     * @param roleIds Ids
     * @return {@link String} List
     */
    List<String> listPermissionsByRoleIds(String roleIds);

}
