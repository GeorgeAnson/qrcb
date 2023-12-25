package com.qrcb.admin.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.qrcb.admin.api.dto.SysLogDto;
import com.qrcb.admin.api.entity.SysLog;
import com.qrcb.admin.api.vo.LogVo;
import com.qrcb.admin.mapper.SysLogMapper;
import com.qrcb.admin.service.SysLogService;
import com.qrcb.common.core.assemble.constant.CommonConstants;
import com.qrcb.common.core.data.tenant.TenantBroker;
import com.qrcb.common.core.data.tenant.TenantContextHolder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 日志表 服务实现类 <br/>
 */

@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveBatchLogs(List<LogVo> logVoList) {
        List<SysLog> sysLogs = logVoList.stream().map(logVo -> {
            SysLog log = new SysLog();
            log.setType(CommonConstants.STATUS_LOCK);
            log.setTitle(logVo.getInfo());
            log.setException(logVo.getStack());
            log.setParams(logVo.getMessage());
            log.setCreateTime(LocalDateTime.now());
            log.setRequestUri(logVo.getUrl());
            log.setCreateBy(logVo.getUser());
            return log;
        }).collect(Collectors.toList());
        return this.saveBatch(sysLogs);
    }

    @Override
    public Page<SysLog> getLogByPage(Page<SysLog> page, SysLogDto sysLogDto) {

        LambdaQueryWrapper<SysLog> wrapper = Wrappers.lambdaQuery();
        if (StrUtil.isNotBlank(sysLogDto.getType())) {
            wrapper.eq(SysLog::getType, sysLogDto.getType());
        }

        if (ArrayUtil.isNotEmpty(sysLogDto.getPeriod())) {
            wrapper.ge(SysLog::getCreateTime, sysLogDto.getPeriod().split(StrUtil.AT)[0])
                    .le(SysLog::getCreateTime, sysLogDto.getPeriod().split(StrUtil.AT)[1]);
        }

        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveLog(SysLogDto sysLogDto) {
        return SqlHelper.retBool((Integer) TenantBroker.applyAs(sysLogDto::getTenantId, tenantId -> {
            TenantContextHolder.setTenantId(tenantId);
            SysLog log = new SysLog();
            BeanUtils.copyProperties(sysLogDto, log);
            return baseMapper.insert(log);
        }));
    }

}
