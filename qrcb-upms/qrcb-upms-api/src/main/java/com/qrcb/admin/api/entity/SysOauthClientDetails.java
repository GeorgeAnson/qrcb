package com.qrcb.admin.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description 客户端信息 <br/>
 */

@Data
@ApiModel(value = "客户端信息")
@EqualsAndHashCode(callSuper = true)
public class SysOauthClientDetails extends Model<SysOauthClientDetails> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 客户端ID
     */
    @NotBlank(message = "client_id 不能为空")
    @ApiModelProperty(value = "客户端id")
    private String clientId;

    /**
     * 客户端密钥
     */
    @NotBlank(message = "client_secret 不能为空")
    @ApiModelProperty(value = "客户端密钥")
    private String clientSecret;

    /**
     * 资源ID
     */
    @ApiModelProperty(value = "资源id列表")
    private String resourceIds;

    /**
     * 作用域
     */
    @NotBlank(message = "scope 不能为空")
    @ApiModelProperty(value = "作用域")
    private String scope;

    /**
     * 授权方式[A,B,C]
     */
    @ApiModelProperty(value = "授权方式")
    private String[] authorizedGrantTypes;

    /**
     * 回调地址
     */
    @ApiModelProperty(value = "回调地址")
    private String webServerRedirectUri;

    /**
     * 权限
     */
    @ApiModelProperty(value = "权限列表")
    private String authorities;

    /**
     * 请求令牌有效时间
     */
    @ApiModelProperty(value = "请求令牌有效时间")
    private Integer accessTokenValidity;

    /**
     * 刷新令牌有效时间
     */
    @ApiModelProperty(value = "刷新令牌有效时间")
    private Integer refreshTokenValidity;

    /**
     * 扩展信息
     */
    @ApiModelProperty(value = "扩展信息")
    private String additionalInformation;

    /**
     * 是否自动放行
     */
    @ApiModelProperty(value = "是否自动放行")
    private String autoApprove;

    /**
     * 删除标记
     */
    @TableLogic
    @ApiModelProperty(value = "删除标记:1=已删除,0=正常")
    private String delFlag;
}
