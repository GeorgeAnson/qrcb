package com.qrcb.common.core.data.datascope;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 数据权限函数类型 <br/>
 */

@Getter
@AllArgsConstructor
public enum DataScopeFuncEnum {

    /**
     * 查询全部数据 SELECT * FROM (originSql) temp_data_scope WHERE temp_data_scope.dept_id IN
     * (1)
     */
    ALL("*", "全部"),

    /**
     * 查询函数COUNT SELECT COUNT(1) FROM (originSql) temp_data_scope WHERE
     * temp_data_scope.dept_id IN (1)
     */
    COUNT("COUNT(1)", "自定义");

    /**
     * 类型
     */
    private final String type;

    /**
     * 描述
     */
    private final String description;

}
