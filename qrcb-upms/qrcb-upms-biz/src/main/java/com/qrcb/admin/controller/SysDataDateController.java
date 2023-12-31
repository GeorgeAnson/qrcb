package com.qrcb.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.admin.api.entity.SysDataDate;
import com.qrcb.admin.service.SysDataDateService;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.log.annotation.SysLog;
import com.qrcb.common.core.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * 系统数据日期
 *
 * @author Anson
 * @Create 2023-12-26 09:32:06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sysDataDate" )
@Api(value = "sysDataDate", tags = "系统数据日期管理")
public class SysDataDateController {

    private final SysDataDateService sysDataDateService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param sysDataDate 系统数据日期
     * @return SysDataDate Page
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/getSysDataDatePage" )
    public R<IPage<SysDataDate>> getSysDataDatePage(Page<SysDataDate> page, SysDataDate sysDataDate) {
        return R.ok(sysDataDateService.page(page, Wrappers.query(sysDataDate)));
    }


    /**
     * 通过id查询系统数据日期
     * @param id id
     * @return R SysDataDate
     */
    @ApiOperation(value = "通过id查询", notes = "通过id查询")
    @GetMapping("/getById/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_sysdatadate_view')" )
    public R<SysDataDate> getById(@PathVariable("id" ) String id) {
        return R.ok(sysDataDateService.getById(id));
    }

    /**
     * 新增系统数据日期
     * @param sysDataDate 系统数据日期
     * @return R Boolean
     */
    @ApiOperation(value = "新增系统数据日期", notes = "新增系统数据日期")
    @SysLog("新增系统数据日期" )
    @PostMapping("/save")
    @PreAuthorize("@pms.hasPermission('admin_sysdatadate_add')" )
    public R<Boolean> save(@RequestBody SysDataDate sysDataDate) {
        sysDataDate.setCreateUser(SecurityUtils.getUser().getUsername());
        sysDataDate.setUpdateUser(SecurityUtils.getUser().getUsername());
        return R.ok(sysDataDateService.save(sysDataDate));
    }

    /**
     * 修改系统数据日期
     * @param sysDataDate 系统数据日期
     * @return R Boolean
     */
    @ApiOperation(value = "修改系统数据日期", notes = "修改系统数据日期")
    @SysLog("修改系统数据日期" )
    @PutMapping("/updateById")
    @PreAuthorize("@pms.hasPermission('admin_sysdatadate_edit')" )
    public R<Boolean> updateById(@RequestBody SysDataDate sysDataDate) {
        sysDataDate.setUpdateUser(SecurityUtils.getUser().getUsername());
        return R.ok(sysDataDateService.updateById(sysDataDate));
    }

    /**
     * 通过id删除系统数据日期
     * @param id id
     * @return R Boolean
     */
    @ApiOperation(value = "通过id删除系统数据日期", notes = "通过id删除系统数据日期")
    @SysLog("通过id删除系统数据日期" )
    @DeleteMapping("/removeById/{id}" )
    @PreAuthorize("@pms.hasPermission('admin_sysdatadate_del')" )
    public R<Boolean> removeById(@PathVariable("id")  String id) {
        return R.ok(sysDataDateService.removeById(id));
    }

}
