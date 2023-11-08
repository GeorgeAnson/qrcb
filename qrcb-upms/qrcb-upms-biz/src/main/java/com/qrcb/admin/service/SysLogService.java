package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.dto.SysLogDto;
import com.qrcb.admin.api.entity.SysLog;
import com.qrcb.admin.api.vo.LogVo;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 日志表 Service 接口 <br/>
 */

public interface SysLogService extends IService<SysLog> {

    /**
     * 批量插入前端错误日志
     *
     * @param logVoList 日志信息
     * @return {@link Boolean}
     */
    Boolean saveBatchLogs(List<LogVo> logVoList);

    /**
     * 分页查询日志
     *
     * @param page      分页对象
     * @param sysLogDto 日志对象
     * @return {@link SysLog} Page
     */
    Page<SysLog> getLogByPage(Page<SysLog> page, SysLogDto sysLogDto);

    /**
     * 插入日志
     *
     * @param sysLogDto 日志对象
     * @return {@link Boolean}
     */
    Boolean saveLog(SysLogDto sysLogDto);

}
