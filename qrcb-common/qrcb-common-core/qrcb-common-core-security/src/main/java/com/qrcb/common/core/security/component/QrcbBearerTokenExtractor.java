package com.qrcb.common.core.security.component;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.authentication.BearerTokenExtractor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author Anson
 * @Create 2023-10-21
 * @Description 改造 {@link BearerTokenExtractor} 对公开权限的请求不进行校验 <br/>
 */

@Component
@RequiredArgsConstructor
public class QrcbBearerTokenExtractor extends BearerTokenExtractor {

    private final PathMatcher pathMatcher = new AntPathMatcher();

    private final PermitAllUrlResolver permitAllUrlResolver;

    @Override
    public Authentication extract(HttpServletRequest request) {

        // 2. 判断请求方法是否匹配
        boolean result = permitAllUrlResolver.getIgnoreUrls().stream().anyMatch(url -> {
            String[] strings = StrUtil.split(url, "|").toArray(new String[0]);
            // 1. 判断路径是否匹配
            boolean match = pathMatcher.match(strings[0], request.getRequestURI());
            // 2. 判断方法是否匹配
            if (strings.length == 2) {
                String[] methods = StrUtil.split(strings[1], StrUtil.COMMA).toArray(new String[0]);
                return ArrayUtil.contains(methods, request.getMethod()) && match;
            }
            return match;
        });
        return result ? null : super.extract(request);
    }

}
