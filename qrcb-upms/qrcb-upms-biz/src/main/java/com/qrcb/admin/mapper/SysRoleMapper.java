package com.qrcb.admin.mapper;

import com.qrcb.admin.api.entity.SysRole;
import com.qrcb.common.core.data.datascope.QrcbBaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 角色表 Mapper 接口<br/>
 */

@Mapper
public interface SysRoleMapper extends QrcbBaseMapper<SysRole> {

    /**
     * 通过用户ID，查询角色信息
     *
     * @param userId 用户ID
     * @return {@link SysRole} List
     */
    List<SysRole> listRolesByUserId(Integer userId);
}
