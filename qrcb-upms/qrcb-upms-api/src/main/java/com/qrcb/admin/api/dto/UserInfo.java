package com.qrcb.admin.api.dto;

import com.qrcb.admin.api.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 用户信息 <br/>
 */

@Data
@ApiModel(value = "用户信息")
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户基本信息
     */
    @ApiModelProperty(value = "用户基本信息")
    private SysUser sysUser;

    /**
     * 权限标识集合
     */
    @ApiModelProperty(value = "权限标识集合")
    private String[] permissions;

    /**
     * 角色集合
     */
    @ApiModelProperty(value = "角色标识集合")
    private Integer[] roles;

}
