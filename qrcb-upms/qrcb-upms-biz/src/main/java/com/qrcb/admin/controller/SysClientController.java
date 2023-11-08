package com.qrcb.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.admin.api.dto.SysOauthClientDetailsDto;
import com.qrcb.admin.api.entity.SysOauthClientDetails;
import com.qrcb.admin.service.SysOauthClientDetailsService;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.log.annotation.SysLog;
import com.qrcb.common.core.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 客户端管理模块 <br/>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/client")
@Api(value = "client", tags = "客户端管理模块")
public class SysClientController {

    private final SysOauthClientDetailsService clientDetailsService;

    /**
     * 通过ID查询
     *
     * @param clientId clientId
     * @return {@link SysOauthClientDetails} List
     */
    @GetMapping("/{clientId}")
    public R<List<SysOauthClientDetails>> getByClientId(@PathVariable String clientId) {
        return R.ok(clientDetailsService.list(Wrappers.<SysOauthClientDetails>lambdaQuery()
                .eq(SysOauthClientDetails::getClientId, clientId)));
    }

    /**
     * 简单分页查询
     *
     * @param page                  分页对象
     * @param sysOauthClientDetails 系统终端
     * @return {@link SysOauthClientDetailsDto} Page
     */
    @GetMapping("/page")
    public R<Page<SysOauthClientDetailsDto>> getOauthClientDetailsPage(Page<SysOauthClientDetails> page, SysOauthClientDetails sysOauthClientDetails) {
        return R.ok(clientDetailsService.queryPage(page, sysOauthClientDetails));
    }

    /**
     * 添加
     *
     * @param clientDetailsDto 实体
     * @return {@link Boolean}
     */
    @SysLog("添加终端")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_client_add')")
    public R<Boolean> add(@Valid @RequestBody SysOauthClientDetailsDto clientDetailsDto) {
        return R.ok(clientDetailsService.saveClient(clientDetailsDto));
    }

    /**
     * 删除
     *
     * @param clientId ID
     * @return {@link Boolean}
     */
    @SysLog("删除终端")
    @DeleteMapping("/{clientId}")
    @PreAuthorize("@pms.hasPermission('sys_client_del')")
    public R<Boolean> removeById(@PathVariable String clientId) {
        return R.ok(clientDetailsService.removeByClientId(clientId));
    }

    /**
     * 编辑
     *
     * @param clientDetailsDto 实体
     * @return {@link Boolean}
     */
    @SysLog("编辑终端")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_client_edit')")
    public R<Boolean> update(@Valid @RequestBody SysOauthClientDetailsDto clientDetailsDto) {
        return R.ok(clientDetailsService.updateClientById(clientDetailsDto));
    }

    /**
     * 获取客户端详情，feign同时开放给外部访问
     *
     * @param clientId 客户端Id
     * @return {@link SysOauthClientDetails}
     */
    @Inner(value = false)
    @GetMapping("/getClientDetailsById/{clientId}")
    public R<SysOauthClientDetails> getClientDetailsById(@PathVariable String clientId) {
        return R.ok(clientDetailsService.getOne(Wrappers.<SysOauthClientDetails>lambdaQuery()
                .eq(SysOauthClientDetails::getClientId, clientId), false));
    }

}
