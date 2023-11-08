package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.dto.SysOauthClientDetailsDto;
import com.qrcb.admin.api.entity.SysOauthClientDetails;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description SysOauthClientDetails Service <br/>
 */

public interface SysOauthClientDetailsService extends IService<SysOauthClientDetails> {

    /**
     * 通过ID删除客户端
     *
     * @param clientId 客户端 ID
     * @return {@link Boolean}
     */
    Boolean removeByClientId(String clientId);

    /**
     * 根据客户端信息
     *
     * @param clientDetailsDto 终端管理传输对象
     * @return {@link Boolean}
     */
    Boolean updateClientById(SysOauthClientDetailsDto clientDetailsDto);

    /**
     * 添加客户端
     *
     * @param clientDetailsDto 终端管理传输对象
     * @return {@link Boolean}
     */
    Boolean saveClient(SysOauthClientDetailsDto clientDetailsDto);

    /**
     * 分页查询客户端信息
     *
     * @param page  分页对象
     * @param query {@link SysOauthClientDetails}
     * @return {@link SysOauthClientDetailsDto} Page
     */
    Page<SysOauthClientDetailsDto> queryPage(Page<SysOauthClientDetails> page, SysOauthClientDetails query);
}
