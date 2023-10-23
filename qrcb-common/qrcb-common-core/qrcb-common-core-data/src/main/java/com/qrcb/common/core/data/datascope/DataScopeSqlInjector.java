package com.qrcb.common.core.data.datascope;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 支持自定义数据权限方法注入 <br/>
 */

public class DataScopeSqlInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> methodList = super.getMethodList(mapperClass);
        methodList.add(new SelectListByScope());
        methodList.add(new SelectPageByScope());
        methodList.add(new SelectCountByScope());
        methodList.add(new InsertBatchSomeColumn());
        return methodList;
    }

}
