package com.qrcb.common.extension.nacos.impl.db2;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import com.alibaba.nacos.plugin.datasource.constants.TableConstant;
import com.alibaba.nacos.plugin.datasource.mapper.AbstractMapper;
import com.alibaba.nacos.plugin.datasource.mapper.ConfigInfoMapper;
import com.qrcb.common.extension.nacos.constant.QrcbDataSourceConstant;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author Anson
 * @Create 2023-10-25
 * @Description <br/>
 */

public class ConfigInfoMapperByDb2 extends AbstractMapper implements ConfigInfoMapper {

    private static final String DATA_ID = "dataId";

    private static final String GROUP = "group";

    private static final String APP_NAME = "appName";

    private static final String CONTENT = "content";

    private static final String TENANT = "tenant";

    @Override
    public String findConfigMaxId() {
        return "SELECT MAX(id) FROM QRCB_CONFIG.CONFIG_INFO";
    }

    @Override
    public String findAllDataIdAndGroup() {
        return "SELECT DISTINCT data_id, group_id FROM QRCB_CONFIG.CONFIG_INFO";
    }

    @Override
    public String findConfigInfoByAppCountRows() {
        return "SELECT count(*) FROM QRCB_CONFIG.CONFIG_INFO WHERE tenant_id LIKE ? AND app_name= ?";
    }

    @Override
    public String findConfigInfoByAppFetchRows(int startRow, int pageSize) {
        return "SELECT id,data_id,group_id,tenant_id,app_name,content FROM QRCB_CONFIG.CONFIG_INFO"
                + " WHERE tenant_id LIKE ? AND app_name= ?" + " LIMIT " + pageSize + " OFFSET " + startRow;
    }

    @Override
    public String configInfoLikeTenantCount() {
        return "SELECT count(*) FROM QRCB_CONFIG.CONFIG_INFO WHERE tenant_id LIKE ?";
    }

    @Override
    public String getTenantIdList(int startRow, int pageSize) {
        return "SELECT tenant_id FROM QRCB_CONFIG.CONFIG_INFO WHERE tenant_id != '' GROUP BY tenant_id LIMIT " + pageSize
                + " OFFSET " + startRow;
    }

    @Override
    public String getGroupIdList(int startRow, int pageSize) {
        return "SELECT group_id FROM QRCB_CONFIG.CONFIG_INFO WHERE tenant_id ='' GROUP BY group_id LIMIT " + pageSize + " OFFSET "
                + startRow;
    }

    @Override
    public String findAllConfigKey(int startRow, int pageSize) {
        return " SELECT data_id,group_id,app_name  FROM ( "
                + " SELECT id FROM QRCB_CONFIG.CONFIG_INFO WHERE tenant_id LIKE ? ORDER BY id LIMIT " + pageSize + " OFFSET "
                + startRow + " )" + " g, QRCB_CONFIG.CONFIG_INFO t WHERE g.id = t.id  ";
    }

    @Override
    public String findAllConfigInfoBaseFetchRows(int startRow, int pageSize) {
        return "SELECT t.id,data_id,group_id,content,md5"
                + " FROM ( SELECT id FROM QRCB_CONFIG.CONFIG_INFO ORDER BY id LIMIT ?,?  ) "
                + " g, QRCB_CONFIG.CONFIG_INFO t  WHERE g.id = t.id ";
    }

    @Override
    public String findAllConfigInfoFragment(int startRow, int pageSize) {
        return "SELECT id,data_id,group_id,tenant_id,app_name,content,md5,gmt_modified,type,encrypted_data_key "
                + "FROM QRCB_CONFIG.CONFIG_INFO WHERE id > ? ORDER BY id ASC LIMIT " + pageSize + " OFFSET " + startRow;
    }

    @Override
    public String findChangeConfig() {
        return "SELECT data_id, group_id, tenant_id, app_name, content, gmt_modified,encrypted_data_key "
                + "FROM QRCB_CONFIG.CONFIG_INFO WHERE gmt_modified >= ? AND gmt_modified <= ?";
    }

    @Override
    public String findChangeConfigCountRows(Map<String, String> params, final Timestamp startTime,
                                            final Timestamp endTime) {
        final String tenant = params.get(TENANT);
        final String dataId = params.get(DATA_ID);
        final String group = params.get(GROUP);
        final String appName = params.get(APP_NAME);
        final String tenantTmp = StringUtils.isBlank(tenant) ? StringUtils.EMPTY : tenant;
        final String sqlCountRows = "SELECT count(*) FROM QRCB_CONFIG.CONFIG_INFO WHERE ";
        String where = " 1=1 ";
        if (!StringUtils.isBlank(dataId)) {
            where += " AND data_id LIKE ? ";
        }
        if (!StringUtils.isBlank(group)) {
            where += " AND group_id LIKE ? ";
        }

        if (!StringUtils.isBlank(tenantTmp)) {
            where += " AND tenant_id = ? ";
        }

        if (!StringUtils.isBlank(appName)) {
            where += " AND app_name = ? ";
        }
        if (startTime != null) {
            where += " AND gmt_modified >=? ";
        }
        if (endTime != null) {
            where += " AND gmt_modified <=? ";
        }
        return sqlCountRows + where;
    }

    @Override
    public String findChangeConfigFetchRows(Map<String, String> params, final Timestamp startTime,
                                            final Timestamp endTime, int startRow, int pageSize, long lastMaxId) {
        final String tenant = params.get(TENANT);
        final String dataId = params.get(DATA_ID);
        final String group = params.get(GROUP);
        final String appName = params.get(APP_NAME);
        final String tenantTmp = StringUtils.isBlank(tenant) ? StringUtils.EMPTY : tenant;
        final String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,app_name,content,type,md5,gmt_modified FROM QRCB_CONFIG.CONFIG_INFO WHERE ";
        String where = " 1=1 ";
        if (!StringUtils.isBlank(dataId)) {
            where += " AND data_id LIKE ? ";
        }
        if (!StringUtils.isBlank(group)) {
            where += " AND group_id LIKE ? ";
        }

        if (!StringUtils.isBlank(tenantTmp)) {
            where += " AND tenant_id = ? ";
        }

        if (!StringUtils.isBlank(appName)) {
            where += " AND app_name = ? ";
        }
        if (startTime != null) {
            where += " AND gmt_modified >=? ";
        }
        if (endTime != null) {
            where += " AND gmt_modified <=? ";
        }
        return sqlFetchRows + where + " AND id > " + lastMaxId + " ORDER BY id ASC" + " LIMIT " + 0 + " OFFSET "
                + pageSize;
    }

    @Override
    public String listGroupKeyMd5ByPageFetchRows(int startRow, int pageSize) {
        return "SELECT t.id,data_id,group_id,tenant_id,app_name,md5,type,gmt_modified,encrypted_data_key FROM "
                + "( SELECT id FROM QRCB_CONFIG.CONFIG_INFO ORDER BY id LIMIT " + pageSize + " OFFSET " + startRow
                + " ) g, QRCB_CONFIG.CONFIG_INFO t WHERE g.id = t.id";
    }

    @Override
    public String findAllConfigInfo4Export(List<Long> ids, Map<String, String> params) {
        String tenant = params.get("tenant");
        String tenantTmp = StringUtils.isBlank(tenant) ? StringUtils.EMPTY : tenant;
        String sql = "SELECT id,data_id,group_id,tenant_id,app_name,content,type,md5,gmt_create,gmt_modified,src_user,src_ip,"
                + "c_desc,c_use,effect,c_schema,encrypted_data_key FROM QRCB_CONFIG.CONFIG_INFO";
        StringBuilder where = new StringBuilder(" WHERE ");
        List<Object> paramList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(ids)) {
            where.append(" id IN (");
            for (int i = 0; i < ids.size(); i++) {
                if (i != 0) {
                    where.append(", ");
                }
                where.append('?');
                paramList.add(ids.get(i));
            }
            where.append(") ");
        }
        else {
            where.append(" tenant_id= ? ");
            paramList.add(tenantTmp);
            if (!StringUtils.isBlank(params.get(DATA_ID))) {
                where.append(" AND data_id LIKE ? ");
            }
            if (StringUtils.isNotBlank(params.get(GROUP))) {
                where.append(" AND group_id= ? ");
            }
            if (StringUtils.isNotBlank(params.get(APP_NAME))) {
                where.append(" AND app_name= ? ");
            }
        }
        return sql + where;
    }

    @Override
    public String findConfigInfoBaseLikeCountRows(Map<String, String> params) {
        final String sqlCountRows = "SELECT count(*) FROM QRCB_CONFIG.CONFIG_INFO WHERE ";
        String where = " 1=1 AND tenant_id='' ";

        if (!StringUtils.isBlank(params.get(DATA_ID))) {
            where += " AND data_id LIKE ? ";
        }
        if (!StringUtils.isBlank(params.get(GROUP))) {
            where += " AND group_id LIKE ";
        }
        if (!StringUtils.isBlank(params.get(CONTENT))) {
            where += " AND content LIKE ? ";
        }
        return sqlCountRows + where;
    }

    @Override
    public String findConfigInfoBaseLikeFetchRows(Map<String, String> params, int startRow, int pageSize) {
        final String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,content FROM QRCB_CONFIG.CONFIG_INFO WHERE ";
        String where = " 1=1 AND tenant_id='' ";
        if (!StringUtils.isBlank(params.get(DATA_ID))) {
            where += " AND data_id LIKE ? ";
        }
        if (!StringUtils.isBlank(params.get(GROUP))) {
            where += " AND group_id LIKE ";
        }
        if (!StringUtils.isBlank(params.get(CONTENT))) {
            where += " AND content LIKE ? ";
        }
        return sqlFetchRows + where + " LIMIT " + startRow + "," + pageSize;
    }

    @Override
    public String findConfigInfo4PageCountRows(Map<String, String> params) {
        final String appName = params.get(APP_NAME);
        final String dataId = params.get(DATA_ID);
        final String group = params.get(GROUP);
        final String sqlCount = "SELECT count(*) FROM QRCB_CONFIG.CONFIG_INFO";
        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(" tenant_id=? ");
        if (StringUtils.isNotBlank(dataId)) {
            where.append(" AND data_id=? ");
        }
        if (StringUtils.isNotBlank(group)) {
            where.append(" AND group_id=? ");
        }
        if (StringUtils.isNotBlank(appName)) {
            where.append(" AND app_name=? ");
        }
        return sqlCount + where;
    }

    @Override
    public String findConfigInfo4PageFetchRows(Map<String, String> params, int startRow, int pageSize) {
        final String appName = params.get(APP_NAME);
        final String dataId = params.get(DATA_ID);
        final String group = params.get(GROUP);
        final String sql = "SELECT id,data_id,group_id,tenant_id,app_name,content,type,encrypted_data_key FROM QRCB_CONFIG.CONFIG_INFO";
        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(" tenant_id=? ");
        if (StringUtils.isNotBlank(dataId)) {
            where.append(" AND data_id=? ");
        }
        if (StringUtils.isNotBlank(group)) {
            where.append(" AND group_id=? ");
        }
        if (StringUtils.isNotBlank(appName)) {
            where.append(" AND app_name=? ");
        }
        return sql + where + " LIMIT " + pageSize + " OFFSET " + startRow;
    }

    @Override
    public String findConfigInfoBaseByGroupFetchRows(int startRow, int pageSize) {
        return "SELECT id,data_id,group_id,content FROM QRCB_CONFIG.CONFIG_INFO WHERE group_id=? AND tenant_id=?" + " LIMIT "
                + pageSize + " OFFSET " + startRow;
    }

    @Override
    public String findConfigInfoLike4PageCountRows(Map<String, String> params) {
        String dataId = params.get(DATA_ID);
        String group = params.get(GROUP);
        final String appName = params.get(APP_NAME);
        final String content = params.get(CONTENT);
        final String sqlCountRows = "SELECT count(*) FROM QRCB_CONFIG.CONFIG_INFO";
        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(" tenant_id LIKE ? ");
        if (!StringUtils.isBlank(dataId)) {
            where.append(" AND data_id LIKE ? ");
        }
        if (!StringUtils.isBlank(group)) {
            where.append(" AND group_id LIKE ? ");
        }
        if (!StringUtils.isBlank(appName)) {
            where.append(" AND app_name = ? ");
        }
        if (!StringUtils.isBlank(content)) {
            where.append(" AND content LIKE ? ");
        }
        return sqlCountRows + where;
    }

    @Override
    public String findConfigInfoLike4PageFetchRows(Map<String, String> params, int startRow, int pageSize) {
        String dataId = params.get(DATA_ID);
        String group = params.get(GROUP);
        final String appName = params.get(APP_NAME);
        final String content = params.get(CONTENT);
        final String sqlFetchRows = "SELECT id,data_id,group_id,tenant_id,app_name,content,encrypted_data_key FROM QRCB_CONFIG.CONFIG_INFO";
        StringBuilder where = new StringBuilder(" WHERE ");
        where.append(" tenant_id LIKE ? ");
        if (!StringUtils.isBlank(dataId)) {
            where.append(" AND data_id LIKE ? ");
        }
        if (!StringUtils.isBlank(group)) {
            where.append(" AND group_id LIKE ? ");
        }
        if (!StringUtils.isBlank(appName)) {
            where.append(" AND app_name = ? ");
        }
        if (!StringUtils.isBlank(content)) {
            where.append(" AND content LIKE ? ");
        }
        return sqlFetchRows + where + " LIMIT " + pageSize + " OFFSET " + startRow;
    }

    @Override
    public String findAllConfigInfoFetchRows(int startRow, int pageSize) {
        return "SELECT t.id,data_id,group_id,tenant_id,app_name,content,md5 "
                + " FROM (  SELECT id FROM QRCB_CONFIG.CONFIG_INFO WHERE tenant_id LIKE ? ORDER BY id LIMIT " + pageSize
                + " OFFSET " + startRow + " )" + " g, config_info t  WHERE g.id = t.id ";
    }

    @Override
    public String findConfigInfosByIds(int idSize) {
        StringBuilder sql = new StringBuilder(
                "SELECT ID,data_id,group_id,tenant_id,app_name,content,md5 FROM QRCB_CONFIG.CONFIG_INFO WHERE ");
        sql.append("id IN (");
        for (int i = 0; i < idSize; i++) {
            if (i != 0) {
                sql.append(", ");
            }
            sql.append('?');
        }
        sql.append(") ");
        return sql.toString();
    }

    @Override
    public String removeConfigInfoByIdsAtomic(int size) {
        StringBuilder sql = new StringBuilder("DELETE FROM QRCB_CONFIG.CONFIG_INFO WHERE ");
        sql.append("id IN (");
        for (int i = 0; i < size; i++) {
            if (i != 0) {
                sql.append(", ");
            }
            sql.append('?');
        }
        sql.append(") ");
        return sql.toString();
    }

    @Override
    public String updateConfigInfoAtomicCas() {
        return "UPDATE QRCB_CONFIG.CONFIG_INFO SET "
                + "content=?, md5 = ?, src_ip=?,src_user=?,gmt_modified=?, app_name=?,c_desc=?,c_use=?,effect=?,type=?,c_schema=? "
                + "WHERE data_id=? AND group_id=? AND tenant_id=? AND (md5=? OR md5 IS NULL OR md5='')";
    }

    @Override
    public String getTableName() {
        return TableConstant.CONFIG_INFO;
    }

    @Override
    public String getDataSource() {
        return QrcbDataSourceConstant.DB2;
    }

}
