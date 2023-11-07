package com.qrcb.admin.api.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 前端角色展示对象 <br/>
 */

@Data
@ApiModel(value = "前端角色展示对象")
public class RoleVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 菜单列表
     */
    private String menuIds;

}
