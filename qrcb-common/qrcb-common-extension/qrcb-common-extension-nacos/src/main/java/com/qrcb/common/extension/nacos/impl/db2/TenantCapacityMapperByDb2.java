package com.qrcb.common.extension.nacos.impl.db2;

import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.plugin.datasource.mapper.TenantCapacityMapper;
import com.qrcb.common.extension.nacos.constant.QrcbDataSourceConstant;

/**
 * @Author Anson
 * @Create 2023-10-25
 * @Description <br/>
 */

public class TenantCapacityMapperByDb2 extends AbstractMapper implements TenantCapacityMapper {

    @Override
    public String getCapacityList4CorrectUsage() {
        return "SELECT id, tenant_id FROM tenant_capacity WHERE id>? LIMIT ?";
    }

    @Override
    public String getTableName() {
        return TableConstant.TENANT_CAPACITY;
    }

    @Override
    public String getDataSource() {
        return QrcbDataSourceConstant.DB2;
    }

}
