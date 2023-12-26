package com.qrcb.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 系统数据日期
 *
 * @author Anson
 * @date 2023-12-26 09:32:06
 */
@Data
@TableName("SYS_DATA_DATE")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统数据日期")
public class SysDataDate extends Model<SysDataDate> {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 数据库模式
     */
    @ApiModelProperty(value="数据库模式")
    private String schema;

    /**
     * 表名
     */
    @ApiModelProperty(value="表名")
    private String tableName;

    /**
     * 字段名
     */
    @ApiModelProperty(value="字段名")
    private String colName;

    /**
     * 是否为实时数据:0=否,1=是
     */
    @ApiModelProperty(value="是否为实时数据:0=否,1=是")
    private String realTime;

    /**
     * 数据日期
     */
    @ApiModelProperty(value="数据日期")
    private LocalDate dataDate;

    /**
     * 实时数据时间
     */
    @ApiModelProperty(value="实时数据时间")
    private LocalDateTime dataDateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    @ApiModelProperty(value="创建人")
    private String createUser;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private LocalDateTime updateTime;

    /**
     * 更新人
     */
    @ApiModelProperty(value="更新人")
    private String updateUser;

    /**
     * 删除标记:1=已删除,0=正常
     */
    @TableLogic
    @ApiModelProperty(value="删除标记:1=已删除,0=正常")
    private String delFlag;

}
