package com.qrcb.common.core.data.mybatis;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.qrcb.common.core.data.datascope.DataScopeHandler;
import com.qrcb.common.core.data.datascope.DataScopeInnerInterceptor;
import com.qrcb.common.core.data.datascope.DataScopeSqlInjector;
import com.qrcb.common.core.data.datascope.QrcbDefaultDataScopeHandler;
import com.qrcb.common.core.data.resolver.SqlFilterArgumentResolver;
import com.qrcb.common.core.data.tenant.QrcbTenantHandler;
import com.qrcb.common.core.security.service.QrcbUser;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.util.List;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description mp 拦截器配置 <br/>
 */

@Configuration
@ConditionalOnBean(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class MybatisPlusConfiguration implements WebMvcConfigurer {

    /**
     * 增加请求参数解析器，对请求中的参数注入SQL 检查
     *
     * @param resolverList
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolverList) {
        resolverList.add(new SqlFilterArgumentResolver());
    }

    /**
     * qrcb 默认数据权限处理器
     *
     * @return QrcbDefaultDataScopeHandler
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(QrcbUser.class)
    public DataScopeHandler dataScopeHandler() {
        return new QrcbDefaultDataScopeHandler();
    }

    /**
     * mybatis plus 拦截器配置
     *
     * @return QrcbDefaultDataScopeHandler
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 多租户支持
        TenantLineInnerInterceptor tenantLineInnerInterceptor = new TenantLineInnerInterceptor();
        tenantLineInnerInterceptor.setTenantLineHandler(qrcbTenantHandler());
        interceptor.addInnerInterceptor(tenantLineInnerInterceptor);
        // 数据权限
        DataScopeInnerInterceptor dataScopeInnerInterceptor = new DataScopeInnerInterceptor();
        dataScopeInnerInterceptor.setDataScopeHandler(dataScopeHandler());
        interceptor.addInnerInterceptor(dataScopeInnerInterceptor);
        // 分页支持
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

    /**
     * 创建租户维护处理器对象
     *
     * @return 处理后的租户维护处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public QrcbTenantHandler qrcbTenantHandler() {
        return new QrcbTenantHandler();
    }

    /**
     * 扩展 mybatis-plus baseMapper 支持数据权限
     *
     * @return {@link DataScopeSqlInjector}
     */
    @Bean
    @ConditionalOnBean(DataScopeHandler.class)
    public DataScopeSqlInjector dataScopeSqlInjector() {
        return new DataScopeSqlInjector();
    }

}
