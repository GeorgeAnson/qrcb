package com.qrcb.admin.api.dto;

import com.qrcb.admin.api.entity.SysUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 系统用户传输对象 <br/>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "系统用户传输对象")
@EqualsAndHashCode(callSuper = true)
public class UserDto extends SysUser {
    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色id集合")
    private List<Integer> role;

    /**
     * 部门id
     */
    @ApiModelProperty(value = "部门id")
    private Integer deptId;

    /**
     * 新密码
     */
    @ApiModelProperty(value = "新密码")
    private String newPassword;
}
