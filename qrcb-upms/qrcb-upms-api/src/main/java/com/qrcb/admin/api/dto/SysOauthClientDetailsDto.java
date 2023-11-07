package com.qrcb.admin.api.dto;

import com.qrcb.admin.api.entity.SysOauthClientDetails;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 终端管理传输对象 <br/>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "终端管理传输对象")
@EqualsAndHashCode(callSuper = true)
public class SysOauthClientDetailsDto extends SysOauthClientDetails {

    private static final long serialVersionUID = 1L;

    /**
     * 验证码开关
     */
    private String captchaFlag;

    /**
     * 前端密码传输是否加密
     */
    private String encFlag;
}
