package com.qrcb.common.core.data.datascope;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description data scope 判断处理器，抽象服务扩展 <br/>
 */

public interface DataScopeHandler {

    /**
     * 计算用户数据权限
     * @param deptList 部门ID，如果为空表示没有任何数据权限。
     * @return 返回true表示无需进行数据过滤处理，返回false表示需要进行数据过滤
     */
    Boolean calcScope(List<Integer> deptList);
}
