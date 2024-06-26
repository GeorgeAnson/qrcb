package com.qrcb.common.extension.nacos.impl.postgresql;

import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.plugin.datasource.mapper.HistoryConfigInfoMapper;
import com.qrcb.common.extension.nacos.constant.QrcbDataSourceConstant;

/**
 * @Author Anson
 * @Create 2023-10-25
 * @Description <br/>
 */

public class HistoryConfigInfoMapperByPostgresql extends AbstractMapper implements HistoryConfigInfoMapper {

    @Override
    public String removeConfigHistory() {
        return "DELETE FROM his_config_info WHERE gmt_modified < ? LIMIT ?";
    }

    @Override
    public String findConfigHistoryCountByTime() {
        return "SELECT count(*) FROM his_config_info WHERE gmt_modified < ?";
    }

    @Override
    public String findDeletedConfig() {
        return "SELECT DISTINCT data_id, group_id, tenant_id FROM his_config_info WHERE op_type = 'D' AND gmt_modified >= ? AND gmt_modified <= ?";
    }

    @Override
    public String findConfigHistoryFetchRows() {
        return "SELECT nid,data_id,group_id,tenant_id,app_name,src_ip,src_user,op_type,gmt_create,gmt_modified FROM his_config_info "
                + "WHERE data_id = ? AND group_id = ? AND tenant_id = ? ORDER BY nid DESC";
    }

    public String pageFindConfigHistoryFetchRows(int pageNo, int pageSize) {
        final int offset = (pageNo - 1) * pageSize;
        final int limit = pageSize;
        return  "SELECT nid,data_id,group_id,tenant_id,app_name,src_ip,src_user,op_type,gmt_create,gmt_modified FROM his_config_info "
                + "WHERE data_id = ? AND group_id = ? AND tenant_id = ? ORDER BY nid DESC  LIMIT " + offset + "," + limit;
    }

    @Override
    public String detailPreviousConfigHistory() {
        return "SELECT nid,data_id,group_id,tenant_id,app_name,content,md5,src_user,src_ip,op_type,gmt_create,gmt_modified "
                + "FROM his_config_info WHERE nid = (SELECT max(nid) FROM his_config_info WHERE id = ?) ";
    }

    @Override
    public String getTableName() {
        return TableConstant.HIS_CONFIG_INFO;
    }

    @Override
    public String getDataSource() {
        return QrcbDataSourceConstant.POSTGRESQL;
    }

}
