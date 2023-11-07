package com.qrcb.admin.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 部门树对象 <br/>
 */

@Data
@ApiModel(value = "部门树")
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends TreeNode {

    @ApiModelProperty(value = "部门名称")
    private String name;

    /**
     * 是否显示被锁定
     */
    private Boolean isLock = true;

}
