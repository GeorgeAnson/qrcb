package com.qrcb.admin.mapper;

import com.qrcb.admin.api.entity.SysUserRole;
import com.qrcb.common.core.data.datascope.QrcbBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 用户角色表 Mapper 接口 <br/>
 */

@Mapper
public interface SysUserRoleMapper extends QrcbBaseMapper<SysUserRole> {

    /**
     * 根据用户Id删除该用户的角色关系
     *
     * @param userId 用户ID
     * @return boolean
     */
    Boolean deleteByUserId(@Param("userId") Integer userId);
}
