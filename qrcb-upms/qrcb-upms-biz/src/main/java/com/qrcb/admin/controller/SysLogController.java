package com.qrcb.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.admin.api.dto.SysLogDto;
import com.qrcb.admin.api.entity.SysLog;
import com.qrcb.admin.api.vo.LogVo;
import com.qrcb.admin.service.SysLogService;
import com.qrcb.common.core.assemble.util.R;
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
 * @Description 日志管理模块 <br/>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/log")
@Api(value = "log", tags = "日志管理模块")
public class SysLogController {

    private final SysLogService sysLogService;

    /**
     * 简单分页查询
     *
     * @param page   分页对象
     * @param sysLogDto 系统日志
     * @return {@link SysLog} Page
     */
    @GetMapping("/page")
    public R<IPage<SysLog>> getLogPage(Page<SysLog> page, SysLogDto sysLogDto) {
        return R.ok(sysLogService.getLogByPage(page, sysLogDto));
    }

    /**
     * 删除日志
     *
     * @param id ID
     * @return {@link Boolean} success/false
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_log_del')")
    public R<Boolean> removeById(@PathVariable Long id) {
        return R.ok(sysLogService.removeById(id));
    }

    /**
     * 插入日志
     *
     * @param sysLog 日志实体
     * @return {@link Boolean} success/false
     */
    @Inner
    @PostMapping("/save")
    public R<Boolean> save(@Valid @RequestBody SysLogDto sysLog) {
        return R.ok(sysLogService.saveLog(sysLog));
    }

    /**
     * 批量插入前端异常日志
     *
     * @param logVoList 日志实体
     * @return {@link Boolean} success/false
     */
    @PostMapping("/logs")
    public R<Boolean> saveBatchLogs(@RequestBody List<LogVo> logVoList) {
        return R.ok(sysLogService.saveBatchLogs(logVoList));
    }

}
