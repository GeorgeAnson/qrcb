package com.qrcb.common.core.security.component;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.qrcb.common.core.assemble.constant.CommonConstants;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.security.exception.QrcbAuth2Exception;
import com.qrcb.common.core.security.service.QrcbUser;
import com.qrcb.common.core.security.util.QrcbSecurityMessageSourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 根据  check token 的结果转化用户信息<br/>
 */

@Slf4j
public class QrcbUserAuthenticationConverter  implements UserAuthenticationConverter {

    private static final String N_A = "N/A";

    /**
     * Extract information about the user to be used in an access token (i.e. for resource
     * servers).
     * @param authentication an authentication representing a user
     * @return a map of key values representing the unique information about the user
     */
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put(USERNAME, authentication.getName());
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }

    /**
     * Inverse of {@link #convertUserAuthentication(Authentication)}. Extracts an
     * Authentication from a map.
     * @param responseMap a map of user information
     * @return an Authentication representing the user or null if there is none
     */
    @Override
    public Authentication extractAuthentication(Map<String, ?> responseMap) {
        if (responseMap.containsKey(USERNAME)) {
            Collection<? extends GrantedAuthority> authorities = getAuthorities(responseMap);
            Map<String, ?> map = MapUtil.get(responseMap, SecurityConstants.DETAILS_USER, Map.class);
            validateTenantId(map);
            String username = MapUtil.getStr(map, SecurityConstants.DETAILS_USERNAME);
            Integer id = MapUtil.getInt(map, SecurityConstants.DETAILS_USER_ID);
            Integer deptId = MapUtil.getInt(map, SecurityConstants.DETAILS_DEPT_ID);
            Integer tenantId = MapUtil.getInt(map, SecurityConstants.DETAILS_TENANT_ID);
            String phone = MapUtil.getStr(map, SecurityConstants.DETAILS_PHONE);
            String avatar = MapUtil.getStr(map, SecurityConstants.DETAILS_AVATAR);
            QrcbUser user = new QrcbUser(id, deptId, phone, avatar, tenantId, username, N_A, true, true, true, true,
                    authorities);
            return new UsernamePasswordAuthenticationToken(user, N_A, authorities);
        }
        return null;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(
                    StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        return AuthorityUtils.NO_AUTHORITIES;
    }

    private void validateTenantId(Map<String, ?> map) {
        String headerValue = getCurrentTenantId();
        Integer userValue = MapUtil.getInt(map, SecurityConstants.DETAILS_TENANT_ID);
        if (StrUtil.isNotBlank(headerValue) && !userValue.toString().equals(headerValue)) {
            log.warn("请求头中的租户ID({})和用户的租户ID({})不一致", headerValue, userValue);
            // TODO: 不要提示租户ID不对，可能被穷举
            throw new QrcbAuth2Exception(QrcbSecurityMessageSourceUtil.getAccessor().getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badTenantId", new Object[] { headerValue },
                    "Bad tenant ID"));
        }
    }

    private Optional<HttpServletRequest> getCurrentHttpRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes()).filter(
                        requestAttributes -> ServletRequestAttributes.class.isAssignableFrom(requestAttributes.getClass()))
                .map(requestAttributes -> ((ServletRequestAttributes) requestAttributes))
                .map(ServletRequestAttributes::getRequest);
    }

    private String getCurrentTenantId() {
        return getCurrentHttpRequest()
                .map(httpServletRequest -> httpServletRequest.getHeader(CommonConstants.TENANT_ID)).orElse(null);
    }

}