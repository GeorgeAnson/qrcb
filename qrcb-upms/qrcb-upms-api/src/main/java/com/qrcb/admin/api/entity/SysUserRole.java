package com.qrcb.admin.api.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 用户角色表 <br/>
 */

@Data
@ApiModel(value = "用户角色")
@EqualsAndHashCode(callSuper = true)
public class SysUserRole extends Model<SysUserRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户id")
    private Integer userId;

    /**
     * 角色ID
     */
    @ApiModelProperty(value = "角色id")
    private Integer roleId;

}
