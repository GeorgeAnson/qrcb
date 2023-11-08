package com.qrcb.admin.mapper;

import com.qrcb.admin.api.entity.SysLog;
import com.qrcb.common.core.data.datascope.QrcbBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 日志表 Mapper 接口 <br/>
 */

@Mapper
public interface SysLogMapper extends QrcbBaseMapper<SysLog> {
}
