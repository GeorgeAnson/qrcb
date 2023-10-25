package com.qrcb.common.extension.nacos.impl.db2;

import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.plugin.datasource.mapper.TenantInfoMapper;
import com.qrcb.common.extension.nacos.constant.QrcbDataSourceConstant;

/**
 * @Author Anson
 * @Create 2023-10-25
 * @Description <br/>
 */

public class TenantInfoMapperByDb2 extends AbstractMapper implements TenantInfoMapper {
    @Override
    public String getTableName() {
        return TableConstant.TENANT_INFO;
    }

    @Override
    public String getDataSource() {
        return QrcbDataSourceConstant.DB2;
    }
}
