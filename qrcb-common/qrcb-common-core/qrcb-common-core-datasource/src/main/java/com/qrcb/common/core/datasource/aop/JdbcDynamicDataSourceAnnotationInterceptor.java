package com.qrcb.common.core.datasource.aop;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.dynamic.datasource.support.DataSourceClassResolver;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.qrcb.common.core.assemble.exception.CheckedException;
import com.qrcb.common.core.assemble.util.SpringContextHolder;
import com.qrcb.common.core.datasource.provider.JdbcDynamicDataSourceProvider;
import lombok.Setter;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @Author Anson
 * @Create 2023-12-31
 * @Description JDBC 多数据源切面方法 <br/>
 * 根据 Key 值和当前活跃的多数据源匹配情况，动态生成新数据源
 */


public class JdbcDynamicDataSourceAnnotationInterceptor extends DynamicDataSourceAnnotationInterceptor {

    private static final String DYNAMIC_PREFIX = "#";
    private static final DataSourceClassResolver RESOLVER = new DataSourceClassResolver();
    private final DataSourceCreator dataSourceCreator;
    private final JdbcDynamicDataSourceProvider jdbcDynamicDataSourceProvider;

    @Setter
    private DsProcessor dsProcessor;

    public JdbcDynamicDataSourceAnnotationInterceptor(DataSourceCreator dataSourceCreator, JdbcDynamicDataSourceProvider jdbcDynamicDataSourceProvider) {
        this.dataSourceCreator = dataSourceCreator;
        this.jdbcDynamicDataSourceProvider = jdbcDynamicDataSourceProvider;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            DynamicDataSourceContextHolder.push(determineDatasource(invocation));
            return invocation.proceed();
        } finally {
            DynamicDataSourceContextHolder.poll();
        }
    }


    private String determineDatasource(MethodInvocation invocation) {
        String key = RESOLVER.findDSKey(invocation.getMethod(), invocation.getThis());
        String finalKey = (!key.isEmpty() && key.startsWith(DYNAMIC_PREFIX)) ? dsProcessor.determineDatasource(invocation, key) : key;

        DynamicDataSourceProperties dynamicDataSourceProperties = SpringContextHolder.getBean(DynamicDataSourceProperties.class);
        if (dynamicDataSourceProperties.getDatasource().keySet().stream().noneMatch(poolName -> poolName.equals(finalKey))
                && !StrUtil.isEmptyOrUndefined(finalKey)) {

            DataSourceProperty dataSourceProperty = jdbcDynamicDataSourceProvider.getDataSourcePropertiesMap().remove(finalKey);
            if (ObjUtil.isNull(dataSourceProperty)) {
                throw new CheckedException(String.format("the datasource pool name : %s is not exist," +
                        " which is specific in annotation @DS(%s).", finalKey, finalKey));
            }
            DynamicRoutingDataSource dynamicRoutingDataSource = SpringContextHolder.getBean(DynamicRoutingDataSource.class);
            dynamicRoutingDataSource.addDataSource(finalKey, dataSourceCreator.createDataSource(dataSourceProperty));
            dynamicDataSourceProperties.getDatasource().putIfAbsent(finalKey, dataSourceProperty);
        }

        return finalKey;
    }
}
