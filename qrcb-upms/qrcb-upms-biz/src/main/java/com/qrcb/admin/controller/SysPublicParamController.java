package com.qrcb.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.admin.api.entity.SysPublicParam;
import com.qrcb.admin.service.SysPublicParamService;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.log.annotation.SysLog;
import com.qrcb.common.core.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 公共参数配置 <br/>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/param")
@Api(value = "param", tags = "公共参数配置")
public class SysPublicParamController {

    private final SysPublicParamService sysPublicParamService;

    /**
     * 通过key查询公共参数值
     *
     * @param publicKey key
     * @return {@link String}
     */
    @Inner(value = false)
    @ApiOperation(value = "查询公共参数值", notes = "根据key查询公共参数值")
    @GetMapping("/publicValue/{publicKey}")
    public R<String> publicKey(@PathVariable("publicKey") String publicKey) {
        return R.ok(sysPublicParamService.getSysPublicParamKeyToValue(publicKey));
    }

    /**
     * 分页查询
     *
     * @param page           分页对象
     * @param sysPublicParam 公共参数
     * @return {@link SysPublicParam}
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R<IPage<SysPublicParam>> getSysPublicParamPage(Page<SysPublicParam> page, SysPublicParam sysPublicParam) {
        return R.ok(sysPublicParamService.page(page, Wrappers.query(sysPublicParam)));
    }

    /**
     * 通过id查询公共参数
     *
     * @param publicId id
     * @return {@link SysPublicParam}
     */
    @ApiOperation(value = "通过id查询公共参数", notes = "通过id查询公共参数")
    @GetMapping("/{publicId}")
    public R<SysPublicParam> getById(@PathVariable("publicId") Long publicId) {
        return R.ok(sysPublicParamService.getById(publicId));
    }

    /**
     * 新增公共参数
     *
     * @param sysPublicParam 公共参数
     * @return {@link Boolean}
     */
    @ApiOperation(value = "新增公共参数", notes = "新增公共参数")
    @SysLog("新增公共参数")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('admin_syspublicparam_add')")
    public R<Boolean> save(@RequestBody SysPublicParam sysPublicParam) {
        return R.ok(sysPublicParamService.save(sysPublicParam));
    }

    /**
     * 修改公共参数
     *
     * @param sysPublicParam 公共参数
     * @return {@link Boolean}
     */
    @ApiOperation(value = "修改公共参数", notes = "修改公共参数")
    @SysLog("修改公共参数")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('admin_syspublicparam_edit')")
    public R<Boolean> updateById(@RequestBody SysPublicParam sysPublicParam) {
        return R.ok(sysPublicParamService.updateParam(sysPublicParam));
    }

    /**
     * 通过id删除公共参数
     *
     * @param publicId id
     * @return {@link Boolean}
     */
    @ApiOperation(value = "删除公共参数", notes = "删除公共参数")
    @SysLog("删除公共参数")
    @DeleteMapping("/{publicId}")
    @PreAuthorize("@pms.hasPermission('admin_syspublicparam_del')")
    public R<Boolean> removeById(@PathVariable Long publicId) {
        return R.ok(sysPublicParamService.removeParam(publicId));
    }

}
