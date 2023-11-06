package com.qrcb.common.extension.nacos.impl.db2;

import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.plugin.datasource.mapper.HistoryConfigInfoMapper;
import com.qrcb.common.extension.nacos.constant.QrcbDataSourceConstant;

/**
 * @Author Anson
 * @Create 2023-10-25
 * @Description <br/>
 */

public class HistoryConfigInfoMapperByDb2 extends AbstractMapper implements HistoryConfigInfoMapper {

    @Override
    public String removeConfigHistory() {
        return "DELETE FROM QRCB_CONFIG.HIS_CONFIG_INFO WHERE gmt_modified < ? LIMIT ?";
    }

    @Override
    public String findConfigHistoryCountByTime() {
        return "SELECT count(*) FROM QRCB_CONFIG.HIS_CONFIG_INFO WHERE gmt_modified < ?";
    }

    @Override
    public String findDeletedConfig() {
        return "SELECT DISTINCT data_id, group_id, tenant_id FROM QRCB_CONFIG.HIS_CONFIG_INFO WHERE op_type = 'D' AND gmt_modified >= ? AND gmt_modified <= ?";
    }

    @Override
    public String findConfigHistoryFetchRows() {
        return "SELECT nid,data_id,group_id,tenant_id,app_name,src_ip,src_user,op_type,gmt_create,gmt_modified FROM QRCB_CONFIG.HIS_CONFIG_INFO "
                + "WHERE data_id = ? AND group_id = ? AND tenant_id = ? ORDER BY nid DESC";
    }

    public String pageFindConfigHistoryFetchRows(int pageNo, int pageSize) {
        final int offset = (pageNo - 1) * pageSize;
        final int limit = pageSize;
        return  "SELECT nid,data_id,group_id,tenant_id,app_name,src_ip,src_user,op_type,gmt_create,gmt_modified FROM QRCB_CONFIG.HIS_CONFIG_INFO "
                + "WHERE data_id = ? AND group_id = ? AND tenant_id = ? ORDER BY nid DESC  LIMIT " + offset + "," + limit;
    }

    @Override
    public String detailPreviousConfigHistory() {
        return "SELECT nid,data_id,group_id,tenant_id,app_name,content,md5,src_user,src_ip,op_type,gmt_create,gmt_modified "
                + "FROM QRCB_CONFIG.HIS_CONFIG_INFO WHERE nid = (SELECT max(nid) FROM QRCB_CONFIG.HIS_CONFIG_INFO WHERE id = ?) ";
    }

    @Override
    public String getTableName() {
        return TableConstant.HIS_CONFIG_INFO;
    }

    @Override
    public String getDataSource() {
        return QrcbDataSourceConstant.DB2;
    }

}
