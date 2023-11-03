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
    public String incrementUsageWithDefaultQuotaLimit() {
        return "UPDATE QRCB_CONFIG.TENANT_CAPACITY SET usage = usage + 1, gmt_modified = ? WHERE tenant_id = ? AND usage <"
                + " ? AND quota = 0";
    }

    @Override
    public String incrementUsageWithQuotaLimit() {
        return "UPDATE QRCB_CONFIG.TENANT_CAPACITY SET usage = usage + 1, gmt_modified = ? WHERE tenant_id = ? AND usage < "
                + "quota AND quota != 0";
    }

    @Override
    public String incrementUsage() {
        return "UPDATE QRCB_CONFIG.TENANT_CAPACITY SET usage = usage + 1, gmt_modified = ? WHERE tenant_id = ?";
    }

    @Override
    public String decrementUsage() {
        return "UPDATE QRCB_CONFIG.TENANT_CAPACITY SET usage = usage - 1, gmt_modified = ? WHERE tenant_id = ? AND usage > 0";
    }

    @Override
    public String correctUsage() {
        return "UPDATE QRCB_CONFIG.TENANT_CAPACITY SET usage = (SELECT count(*) FROM QRCB_CONFIG.CONFIG_INFO WHERE tenant_id = ?), "
                + "gmt_modified = ? WHERE tenant_id = ?";
    }

    @Override
    public String getCapacityList4CorrectUsage() {
        return "SELECT id, tenant_id FROM QRCB_CONFIG.TENANT_CAPACITY WHERE id>? LIMIT ?";
    }

    @Override
    public String insertTenantCapacity() {
        return "INSERT INTO QRCB_CONFIG.TENANT_CAPACITY (tenant_id, quota, usage, max_size, max_aggr_count, max_aggr_size, "
                + "gmt_create, gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM QRCB_CONFIG.CONFIG_INFO WHERE tenant_id=?;";
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
