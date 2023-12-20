package com.qrcb.common.core.security.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @Author Anson
 * @Create 2023-10-20
 * @Description 扩展用户信息 <br/>
 */

public class QrcbUser extends User {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    /**
     * 用户ID
     */
    @Getter
    private Integer id;

    /**
     * 部门ID
     */
    @Getter
    private Integer deptId;

    /**
     * 手机号
     */
    @Getter
    private String phone;

    /**
     * 姓名
     */
    @Getter
    private String realName;

    /**
     * 头像
     */
    @Getter
    private String avatar;

    /**
     * 租户ID
     */
    @Getter
    private Integer tenantId;

    /**
     * Construct the <code>User</code> with the details required by
     * {@link DaoAuthenticationProvider}.
     * @param id 用户ID
     * @param deptId 部门ID
     * @param tenantId 租户ID
     * @param username the username presented to the
     * <code>DaoAuthenticationProvider</code>
     * @param password the password that should be presented to the
     * <code>DaoAuthenticationProvider</code>
     * @param enabled set to <code>true</code> if the user is enabled
     * @param accountNonExpired set to <code>true</code> if the account has not expired
     * @param credentialsNonExpired set to <code>true</code> if the credentials have not
     * expired
     * @param accountNonLocked set to <code>true</code> if the account is not locked
     * @param authorities the authorities that should be granted to the caller if they
     * presented the correct username and password and the user is enabled. Not null.
     * @throws IllegalArgumentException if a <code>null</code> value was passed either as
     * a parameter or as an element in the <code>GrantedAuthority</code> collection
     */
    @JsonCreator
    public QrcbUser(@JsonProperty("id") Integer id, @JsonProperty("deptId") Integer deptId,
                    @JsonProperty("phone") String phone, @JsonProperty("avatar") String avatar,
                    @JsonProperty("tenantId") Integer tenantId, @JsonProperty("username") String username,
                    @JsonProperty("password") String password, @JsonProperty("realName") String realName,
                    @JsonProperty("enabled") boolean enabled,
                    @JsonProperty("accountNonExpired") boolean accountNonExpired,
                    @JsonProperty("credentialsNonExpired") boolean credentialsNonExpired,
                    @JsonProperty("accountNonLocked") boolean accountNonLocked,
                    @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.deptId = deptId;
        this.phone = phone;
        this.realName=realName;
        this.avatar = avatar;
        this.tenantId = tenantId;
    }

}
