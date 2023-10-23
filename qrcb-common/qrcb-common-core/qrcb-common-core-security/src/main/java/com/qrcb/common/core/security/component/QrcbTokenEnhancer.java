package com.qrcb.common.core.security.component;

import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.security.service.QrcbUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description token 增强，客户端模式不增强 <br/>
 */

public class QrcbTokenEnhancer implements TokenEnhancer {

    /**
     * Provides an opportunity for customization of an access token (e.g. through its
     * additional information map) during the process of creating a new token for use by a
     * client.
     * @param accessToken the current access token with its expiration and refresh token
     * @param authentication the current authentication including client and user details
     * @return a new token enhanced with additional information
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if (SecurityConstants.CLIENT_CREDENTIALS.equals(authentication.getOAuth2Request().getGrantType())) {
            return accessToken;
        }

        final Map<String, Object> additionalInfo = new HashMap<>(8);
        QrcbUser qrcbUser = (QrcbUser) authentication.getUserAuthentication().getPrincipal();
        additionalInfo.put(SecurityConstants.DETAILS_USER, qrcbUser);
        additionalInfo.put(SecurityConstants.DETAILS_LICENSE, SecurityConstants.QRCB_LICENSE);
        additionalInfo.put(SecurityConstants.ACTIVE, Boolean.TRUE);
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }

}
