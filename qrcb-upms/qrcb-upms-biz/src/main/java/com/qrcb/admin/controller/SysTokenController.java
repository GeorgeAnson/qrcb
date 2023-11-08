package com.qrcb.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.admin.api.feign.RemoteTokenService;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.log.annotation.SysLog;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 令牌管理模块 <br/>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/token")
@Api(value = "token", tags = "令牌管理模块")
public class SysTokenController {

    private final RemoteTokenService remoteTokenService;

    /**
     * 分页token 信息
     *
     * @param params 参数集
     * @return token集合
     */
    @GetMapping("/page")
    public R<Page> getTokenPage(@RequestParam Map<String, Object> params) {
        // 获取请求的
        return remoteTokenService.getTokenPage(params, SecurityConstants.FROM_IN);
    }

    /**
     * 删除
     *
     * @param token getTokenPage
     * @return {@link Boolean} success/false
     */
    @SysLog("删除用户token")
    @DeleteMapping("/{token}")
    @PreAuthorize("@pms.hasPermission('sys_token_del')")
    public R<Boolean> removeById(@PathVariable String token) {
        return remoteTokenService.removeTokenById(token, SecurityConstants.FROM_IN);
    }

}
