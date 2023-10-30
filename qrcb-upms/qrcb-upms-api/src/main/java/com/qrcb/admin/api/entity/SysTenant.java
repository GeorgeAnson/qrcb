package com.qrcb.admin.api.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 租户信息 <br/>
 */

@Data
@ApiModel(value = "租户信息")
@EqualsAndHashCode(callSuper = true)
public class SysTenant extends Model<SysTenant>  {

    private static final long serialVersionUID = 1L;

    /**
     * 租户id
     */
    @TableId
    @ApiModelProperty(value = "租户id")
    private Integer id;

    /**
     * 租户名称
     */
    @ApiModelProperty(value = "租户名称")
    private String name;

    /**
     * 租户编号
     */
    @ApiModelProperty(value = "租户编号")
    private String code;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endTime;

    /**
     * 0正常 9-冻结
     */
    @ApiModelProperty(value = "租户冻结标记,9:冻结,0:正常")
    private String status;

    /**
     * 删除标记
     */
    @TableLogic
    @ApiModelProperty(value = "删除标记,1:已删除,0:正常")
    private String delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

}
