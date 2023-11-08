package com.qrcb.admin.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.admin.api.dto.UserDto;
import com.qrcb.admin.api.entity.SysUser;
import com.qrcb.admin.api.vo.UserVo;
import com.qrcb.common.core.data.datascope.DataScope;
import com.qrcb.common.core.data.datascope.QrcbBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 用户表 Mapper 接口 <br/>
 */

@Mapper
public interface SysUserMapper extends QrcbBaseMapper<SysUser> {

    /**
     * 通过用户名查询用户信息（含有角色信息）
     *
     * @param username 用户名
     * @return {@link UserVo}
     */
    UserVo getUserVoByUsername(String username);

    /**
     * 分页查询用户信息（含角色）
     *
     * @param page      分页
     * @param userDto   查询参数
     * @param dataScope 数据权限
     * @return {@link UserVo} Page
     */
    IPage<UserVo> getUserVosPage(Page page, @Param("query") UserDto userDto, DataScope dataScope);

    /**
     * 通过ID查询用户信息
     *
     * @param id 用户ID
     * @return {@link UserVo}
     */
    UserVo getUserVoById(Integer id);

}
