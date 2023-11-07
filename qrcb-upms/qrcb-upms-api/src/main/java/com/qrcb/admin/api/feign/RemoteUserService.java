package com.qrcb.admin.api.feign;

import com.qrcb.admin.api.dto.UserInfo;
import com.qrcb.admin.api.entity.SysUser;
import com.qrcb.common.core.assemble.constant.SecurityConstants;
import com.qrcb.common.core.assemble.constant.ServiceNameConstants;
import com.qrcb.common.core.assemble.util.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description <br/>
 */

@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.UPMS_SERVICE)
public interface RemoteUserService {

    /**
     * 通过用户名查询用户、角色信息
     *
     * @param username 用户名
     * @param from     调用标志
     * @return R {@link UserInfo}
     */
    @GetMapping("/user/info/{username}")
    R<UserInfo> info(@PathVariable("username") String username, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 通过社交账号或手机号查询用户、角色信息
     *
     * @param inStr appid@code
     * @param from  调用标志
     * @return R {@link UserInfo}
     */
    @GetMapping("/social/info/{inStr}")
    R<UserInfo> social(@PathVariable("inStr") String inStr, @RequestHeader(SecurityConstants.FROM) String from);

    /**
     * 查询上级部门的用户信息
     *
     * @param username 用户名
     * @return R {@link SysUser} List
     */
    @GetMapping("/user/ancestor/{username}")
    R<List<SysUser>> ancestorUsers(@PathVariable("username") String username);

}
