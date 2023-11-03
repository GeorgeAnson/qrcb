package com.qrcb.common.extension.nacos.impl.db2;

import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoAggrMapper;
import com.qrcb.common.extension.nacos.constant.QrcbDataSourceConstant;

/**
 * @Author Anson
 * @Create 2023-10-25
 * @Description <br/>
 */

public class ConfigInfoAggrMapperByDb2 extends AbstractMapper implements ConfigInfoAggrMapper {

    /**
     * 批量删除
     *
     * @param datumSize
     * @return
     */
    @Override
    public String batchRemoveAggr(int datumSize) {
        final StringBuilder placeholderString = new StringBuilder();
        for (int i = 0; i < datumSize; i++) {
            if (i != 0) {
                placeholderString.append(", ");
            }
            placeholderString.append('?');
        }
        return "DELETE FROM QRCB_CONFIG.CONFIG_INFO_AGGR WHERE data_id = ? AND group_id = ? AND tenant_id = ? AND datum_id IN ("
                + placeholderString + ")";
    }

    @Override
    public String aggrConfigInfoCount(int size, boolean isIn) {
        StringBuilder sql = new StringBuilder(
                "SELECT count(*) FROM QRCB_CONFIG.CONFIG_INFO_AGGR WHERE data_id = ? AND group_id = ? AND tenant_id = ? AND datum_id");
        if (isIn) {
            sql.append(" IN (");
        } else {
            sql.append(" NOT IN (");
        }
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sql.append(", ");
            }
            sql.append('?');
        }
        sql.append(')');

        return sql.toString();
    }

    @Override
    public String findConfigInfoAggrIsOrdered() {
        return "SELECT data_id,group_id,tenant_id,datum_id,app_name,content FROM "
                + "QRCB_CONFIG.CONFIG_INFO_AGGR WHERE data_id = ? AND group_id = ? AND tenant_id = ? ORDER BY datum_id";
    }

    @Override
    public String findConfigInfoAggrByPageFetchRows(int startRow, int pageSize) {
        return "SELECT data_id,group_id,tenant_id,datum_id,app_name,content FROM QRCB_CONFIG.CONFIG_INFO_AGGR WHERE data_id= ? AND "
                + "group_id= ? AND tenant_id= ? ORDER BY datum_id LIMIT " + pageSize + " OFFSET " + startRow;
    }

    @Override
    public String findAllAggrGroupByDistinct() {
        return "SELECT DISTINCT data_id, group_id, tenant_id FROM QRCB_CONFIG.CONFIG_INFO_AGGR";
    }

    @Override
    public String getTableName() {
        return TableConstant.CONFIG_INFO_AGGR;
    }

    @Override
    public String getDataSource() {
        return QrcbDataSourceConstant.DB2;
    }
}
