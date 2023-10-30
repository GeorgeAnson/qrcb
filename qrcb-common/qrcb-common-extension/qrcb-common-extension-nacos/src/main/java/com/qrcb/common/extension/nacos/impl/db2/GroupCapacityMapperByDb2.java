package com.qrcb.common.extension.nacos.impl.db2;

import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.plugin.datasource.mapper.GroupCapacityMapper;
import com.qrcb.common.extension.nacos.constant.QrcbDataSourceConstant;

/**
 * @Author Anson
 * @Create 2023-10-25
 * @Description <br/>
 */

public class GroupCapacityMapperByDb2 extends AbstractMapper implements GroupCapacityMapper {

    @Override
    public String insertIntoSelectByWhere() {
        return "INSERT INTO group_capacity (group_id, quota,usage, max_size, max_aggr_count, max_aggr_size, gmt_create,"
                + " gmt_modified) SELECT ?, ?, count(*), ?, ?, ?, ?, ? FROM config_info WHERE group_id=? AND tenant_id = ''";
    }

    @Override
    public String updateUsageByWhere() {
        return "UPDATE group_capacity SET usage = (SELECT count(*) FROM config_info WHERE group_id=? AND tenant_id = ''),"
                + " gmt_modified = ? WHERE group_id= ?";
    }

    @Override
    public String selectGroupInfoBySize() {
        return "SELECT id, group_id FROM group_capacity WHERE id > ? LIMIT ?";
    }

    @Override
    public String getTableName() {
        return TableConstant.GROUP_CAPACITY;
    }

    @Override
    public String getDataSource() {
        return QrcbDataSourceConstant.DB2;
    }

}
