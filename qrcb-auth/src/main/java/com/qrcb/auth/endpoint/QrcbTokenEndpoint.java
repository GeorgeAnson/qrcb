package com.qrcb.auth.endpoint;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.admin.api.entity.SysTenant;
import com.qrcb.admin.api.feign.RemoteTenantService;
import com.qrcb.auth.service.QrcbTokenDealServiceImpl;
import com.qrcb.common.core.assemble.constant.PaginationConstants;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.security.annotation.Inner;
import com.qrcb.common.core.security.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.AuthorizationRequest;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @Author Anson
 * @Create 2023-10-30
 * @Description token 端点 <br/>
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/token")
public class QrcbTokenEndpoint {

    private final ClientDetailsService clientDetailsService;

    private final QrcbTokenDealServiceImpl dealService;

    private final RemoteTenantService tenantService;


    /**
     * 认证页面
     *
     * @param modelAndView {@link ModelAndView}
     * @param error        表单登录失败处理回调的错误信息
     * @return ModelAndView {@link ModelAndView}
     */
    @GetMapping("/login")
    public ModelAndView require(ModelAndView modelAndView, @RequestParam(required = false) String error) {
        R<List<SysTenant>> tenantList = tenantService.list(SecurityConstants.FROM_IN);
        modelAndView.setViewName("ftl/login");
        modelAndView.addObject("error", error);
        modelAndView.addObject("tenantList", tenantList.getData());
        return modelAndView;
    }

    /**
     * 确认授权页面
     *
     * @param request      {@link HttpServletRequest}
     * @param session      {@link HttpSession}
     * @param modelAndView {@link ModelAndView}
     * @return {@link ModelAndView}
     */
    @GetMapping("/confirm_access")
    public ModelAndView confirm(HttpServletRequest request, HttpSession session, ModelAndView modelAndView) {
        Map<String, Object> scopeList = (Map<String, Object>) request.getAttribute("scopes");
        modelAndView.addObject("scopeList", scopeList.keySet());

        Object auth = session.getAttribute("authorizationRequest");
        if (auth != null) {
            AuthorizationRequest authorizationRequest = (AuthorizationRequest) auth;
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(authorizationRequest.getClientId());
            modelAndView.addObject("app", clientDetails.getAdditionalInformation());
            modelAndView.addObject("user", SecurityUtils.getUser());
        }

        modelAndView.setViewName("ftl/confirm");
        return modelAndView;
    }

    /**
     * 退出token
     *
     * @param authHeader Authorization
     */
    @DeleteMapping("/logout")
    public R logout(@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authHeader) {
        if (StrUtil.isBlank(authHeader)) {
            return R.ok(Boolean.FALSE, "退出失败，token 为空");
        }

        String tokenValue = authHeader.replace(OAuth2AccessToken.BEARER_TYPE, StrUtil.EMPTY).trim();
        return delToken(tokenValue);
    }

    /**
     * 令牌管理调用
     *
     * @param token token
     * @return Boolean
     */
    @Inner
    @DeleteMapping("/{token}")
    public R<Boolean> delToken(@PathVariable("token") String token) {
        return dealService.removeToken(token);
    }

    /**
     * 查询token
     *
     * @param params 分页参数
     * @return Page
     */
    @Inner
    @PostMapping("/page")
    public R<Page> tokenList(@RequestBody Map<String, Object> params) {
        Page result = new Page(MapUtil.getInt(params, PaginationConstants.CURRENT),
                MapUtil.getInt(params, PaginationConstants.SIZE));

        // 根据 username 查询 token 列表
        Object username = params.get("username");
        if (username != null) {
            return dealService.queryTokenByUsername(result, (String) username);
        }

        return dealService.queryToken(result);
    }

}
