package com.qrcb.common.core.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @Author Anson
 * @Create 2023-10-20
 * @Description <br/>
 */

public interface QrcbUserDetailsService extends UserDetailsService {

    /**
     * 根据社交登录code 登录
     * @param code TYPE@CODE
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    UserDetails loadUserBySocial(String code) throws UsernameNotFoundException;

}
