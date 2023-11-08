package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.entity.SysTenant;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description SysTenant Service <br/>
 */

public interface SysTenantService extends IService<SysTenant> {

    /**
     * 获取正常状态租户
     * <p>
     * 1. 状态正常 2. 开始时间小于等于当前时间 3. 结束时间大于等于当前时间
     *
     * @return {@link SysTenant} List
     */
    List<SysTenant> getNormalTenant();

    /**
     * 保存租户
     * <p>
     * 1. 保存租户 2. 初始化权限相关表 - sys_user - sys_role - sys_menu - sys_user_role -
     * sys_role_menu - sys_dict - sys_dict_item - sys_client_details - sys_public_params
     *
     * @param sysTenant 租户实体
     * @return {@link Boolean}
     */
    Boolean saveTenant(SysTenant sysTenant);

}
