package com.qrcb.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.admin.api.entity.SysTenant;
import com.qrcb.admin.service.SysTenantService;
import com.qrcb.common.core.assemble.constant.CacheConstants;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.log.annotation.SysLog;
import com.qrcb.common.core.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 租户管理 <br/>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/tenant")
@Api(value = "tenant", tags = "租户管理")
public class SysTenantController {

    private final SysTenantService sysTenantService;

    /**
     * 分页查询
     *
     * @param page      分页对象
     * @param sysTenant 租户
     * @return {@link SysTenant} Page
     */
    @GetMapping("/page")
    public R<IPage<SysTenant>> getSysTenantPage(Page<SysTenant> page, SysTenant sysTenant) {
        return R.ok(sysTenantService.page(page, Wrappers.query(sysTenant)));
    }

    /**
     * 通过id查询租户
     *
     * @param id id
     * @return {@link SysTenant}
     */
    @GetMapping("/{id}")
    public R<SysTenant> getById(@PathVariable("id") Integer id) {
        return R.ok(sysTenantService.getById(id));
    }

    /**
     * 新增租户
     *
     * @param sysTenant 租户
     * @return {@link Boolean}
     */
    @SysLog("新增租户")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_systenant_add')")
    @CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
    public R<Boolean> save(@RequestBody SysTenant sysTenant) {
        return R.ok(sysTenantService.saveTenant(sysTenant));
    }

    /**
     * 修改租户
     *
     * @param sysTenant 租户
     * @return {@link Boolean}
     */
    @SysLog("修改租户")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_systenant_edit')")
    @CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
    public R<Boolean> updateById(@RequestBody SysTenant sysTenant) {
        return R.ok(sysTenantService.updateById(sysTenant));
    }

    /**
     * 通过id删除租户
     *
     * @param id id
     * @return {@link Boolean}
     */
    @SysLog("删除租户")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('admin_systenant_del')")
    @CacheEvict(value = CacheConstants.TENANT_DETAILS, allEntries = true)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return R.ok(sysTenantService.removeById(id));
    }

    /**
     * 查询全部有效的租户
     *
     * @return {@link SysTenant} List
     */
    @Inner(value = false)
    @GetMapping("/list")
    public R<List<SysTenant>> list() {
        return R.ok(sysTenantService.getNormalTenant().stream()
                .filter(tenant -> tenant.getStartTime().isBefore(LocalDateTime.now()))
                .filter(tenant -> tenant.getEndTime().isAfter(LocalDateTime.now())).collect(Collectors.toList()));
    }
}
