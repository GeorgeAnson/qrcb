package com.qrcb.admin.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 公共参数配置 <br/>
 */

@Data
@ApiModel(value = "公共参数")
@EqualsAndHashCode(callSuper = true)
public class SysPublicParam extends Model<SysPublicParam> {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId
    @ApiModelProperty(value = "公共参数编号")
    private Long publicId;

    /**
     * 公共参数名称
     */
    @ApiModelProperty(value = "公共参数名称", required = true, example = "公共参数名称")
    private String publicName;

    /**
     * 公共参数地址值,英文大写+下划线
     */
    @ApiModelProperty(value = "键[英文大写+下划线]", required = true, example = "QRCB_PUBLIC_KEY")
    private String publicKey;

    /**
     * 值
     */
    @ApiModelProperty(value = "值", required = true, example = "999")
    private String publicValue;

    /**
     * 状态（1=有效,2=无效）
     */
    @ApiModelProperty(value = "标识:1=有效,2=无效", example = "1")
    private String status;

    /**
     * 删除状态（0：正常 1：已删除）
     */
    @TableLogic
    @ApiModelProperty(value = "状态:0=正常,1=删除", example = "0")
    private String delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间", example = "2019-03-21 12:28:48")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间", example = "2019-03-21 12:28:48")
    private Date updateTime;

    /**
     * 是否是系统内置
     */
    @TableField(value = "system")
    @ApiModelProperty(value = "是否是系统内置")
    private String system;

    /**
     * 配置类型：0=默认,1=检索,2=原文,3=报表,4=安全,5=文档,6=消息,9=其他
     */
    @ApiModelProperty(value = "类型:1=检索,2=原文...", example = "1")
    private String publicType;

}
