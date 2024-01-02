package com.qrcb.common.core.datasource.aop;

import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationAdvisor;
import lombok.NonNull;

/**
 * @Author Anson
 * @Create 2024-01-02
 * @Description JDBC 多数据源切面构造 <br/>
 */

public class JdbcDynamicDataSourceAnnotationAdvisor  extends DynamicDataSourceAnnotationAdvisor {

    private static final long serialVersionUID = 1569252970122786430L;

    public JdbcDynamicDataSourceAnnotationAdvisor(@NonNull JdbcDynamicDataSourceAnnotationInterceptor jdbcDynamicDataSourceAnnotationInterceptor) {
        super(jdbcDynamicDataSourceAnnotationInterceptor);
    }
}
