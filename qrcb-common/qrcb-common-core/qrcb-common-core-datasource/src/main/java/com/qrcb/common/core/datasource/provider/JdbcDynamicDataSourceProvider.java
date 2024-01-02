package com.qrcb.common.core.datasource.provider;

import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.qrcb.common.core.datasource.config.HikariDataSourceProperties;
import com.qrcb.common.core.datasource.support.DataSourceConstants;
import com.qrcb.common.core.datasource.util.DsConfTypeEnum;
import com.qrcb.common.core.datasource.util.DsJdbcUrlEnum;
import lombok.Getter;
import org.jasypt.encryption.StringEncryptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Anson
 * @Create 2023-10-24
 * @Description 从数据源中获取配置信息 <br/>
 * 仅直接配置生成默认主数据源，其他动态多数据源配置根据 Key 条件动态创建
 */

public class JdbcDynamicDataSourceProvider extends AbstractJdbcDataSourceProvider {

    private final HikariDataSourceProperties properties;

    private final StringEncryptor stringEncryptor;

    @Getter
    private Map<String, DataSourceProperty> dataSourcePropertiesMap;

    public JdbcDynamicDataSourceProvider(StringEncryptor stringEncryptor, HikariDataSourceProperties properties) {
        super(properties.getDriverClassName(), properties.getUrl(), properties.getUsername(), properties.getPassword());
        this.stringEncryptor = stringEncryptor;
        this.properties = properties;
        this.dataSourcePropertiesMap=new HashMap<>(8);
    }

    /**
     * 执行语句获得数据源参数
     *
     * @param statement 语句
     * @return 数据源参数
     * @throws SQLException sql异常
     */
    @Override
    protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
        ResultSet rs = statement.executeQuery(properties.getQueryDsSql());

        while (rs.next()) {
            String name = rs.getString(DataSourceConstants.NAME);
            String username = rs.getString(DataSourceConstants.DS_USER_NAME);
            String password = rs.getString(DataSourceConstants.DS_USER_PWD);
            Integer confType = rs.getInt(DataSourceConstants.DS_CONFIG_TYPE);
            String dsType = rs.getString(DataSourceConstants.DS_TYPE);

            String url;
            // JDBC 配置形式
            DsJdbcUrlEnum urlEnum = DsJdbcUrlEnum.get(dsType);
            if (DsConfTypeEnum.JDBC.getType().equals(confType)) {
                url = rs.getString(DataSourceConstants.DS_JDBC_URL);
            } else if (DsJdbcUrlEnum.MSSQL.getDbName().equals(dsType)) {
                String host = rs.getString(DataSourceConstants.DS_HOST);
                String port = rs.getString(DataSourceConstants.DS_PORT);
                String dsName = rs.getString(DataSourceConstants.DS_NAME);
                String instance = rs.getString(DataSourceConstants.DS_INSTANCE);
                url = String.format(urlEnum.getUrl(), host, instance, port, dsName);
            } else {
                String host = rs.getString(DataSourceConstants.DS_HOST);
                String port = rs.getString(DataSourceConstants.DS_PORT);
                String dsName = rs.getString(DataSourceConstants.DS_NAME);
                url = String.format(urlEnum.getUrl(), host, port, dsName);
            }

            DataSourceProperty property = new DataSourceProperty();
            property.setUsername(username);
            property.setPassword(stringEncryptor.decrypt(password));
            property.setUrl(url);
            this.dataSourcePropertiesMap.put(name, property);
        }

        // 添加默认主数据源
        Map<String, DataSourceProperty> map = new HashMap<>(8);
        DataSourceProperty property = new DataSourceProperty();
        property.setUsername(properties.getUsername());
        property.setPassword(properties.getPassword());
        property.setUrl(properties.getUrl());
        map.put(DataSourceConstants.DS_MASTER, property);
        return map;
    }

}
