package com.qrcb.common.core.data.datascope;

import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 扩展支持COUNT查询数量 <br/>
 */

public class SelectCountByScope extends AbstractMethod {

    private static final long serialVersionUID = -1081226594595231062L;

    public SelectCountByScope() {
        this("selectCountByScope");
    }

    /**
     * @param methodName 方法名
     * @since 3.5.0
     */
    public SelectCountByScope(String methodName) {
        super(methodName);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.SELECT_COUNT;

        String sql = String.format(sqlMethod.getSql(), sqlFirst(), sqlCount(), tableInfo.getTableName(),
                sqlWhereEntityWrapper(true, tableInfo), sqlComment());
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);

        return this.addSelectMappedStatementForOther(mapperClass, methodName, sqlSource, Long.class);
    }

}