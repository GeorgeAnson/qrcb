package com.qrcb.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.dto.UserDto;
import com.qrcb.admin.api.dto.UserInfo;
import com.qrcb.admin.api.entity.SysUser;
import com.qrcb.admin.api.vo.UserVo;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 用户表 Service 接口 <br/>
 */

public interface SysUserService extends IService<SysUser> {

    /**
     * 查询用户信息
     *
     * @param sysUser 用户
     * @return {@link UserInfo}
     */
    UserInfo findUserInfo(SysUser sysUser);

    /**
     * 分页查询用户信息（含有角色信息）
     *
     * @param page    分页对象
     * @param userDto 参数列表
     * @return {@link Boolean} Page
     */
    IPage<UserVo> getUsersWithRolePage(Page page, UserDto userDto);

    /**
     * 删除用户
     *
     * @param sysUser 用户
     * @return {@link Boolean}
     */
    Boolean deleteUserById(SysUser sysUser);

    /**
     * 更新当前用户基本信息
     *
     * @param userDto 用户信息
     * @return {@link Boolean}
     */
    Boolean updateUserInfo(UserDto userDto);

    /**
     * 更新指定用户信息
     *
     * @param userDto 用户信息
     * @return {@link Boolean}
     */
    Boolean updateUser(UserDto userDto);

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return {@link UserVo}
     */
    UserVo selectUserVoById(Integer id);

    /**
     * 查询上级部门的用户信息
     *
     * @param username 用户名
     * @return {@link SysUser} List
     */
    List<SysUser> listAncestorUsers(String username);

    /**
     * 保存用户信息
     *
     * @param userDto 用户对象
     * @return {@link Boolean}
     */
    Boolean saveUser(UserDto userDto);
}
