package com.qrcb.admin.controller;

import com.qrcb.admin.service.MobileService;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 手机验证码 <br/>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/mobile")
@Api(value = "mobile", tags = "手机管理模块")
public class MobileController {


    private final MobileService mobileService;

    /**
     * 发送验证码
     *
     * @param mobile 手机号
     * @return {@link String} 验证码
     */
    @Inner(value = false)
    @GetMapping("/{mobile}")
    public R<String> sendSmsCode(@PathVariable String mobile) {
        return R.ok(mobileService.sendSmsCode(mobile));
    }
}
