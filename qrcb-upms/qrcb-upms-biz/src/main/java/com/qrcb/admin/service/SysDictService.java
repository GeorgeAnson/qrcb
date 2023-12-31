package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.entity.SysDict;
import com.qrcb.common.core.assemble.util.R;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 字典表 服务类 <br/>
 */

public interface SysDictService extends IService<SysDict> {

    /**
     * 根据ID 删除字典
     *
     * @param id
     * @return Boolean
     */
    R<Boolean> removeDict(Integer id);

    /**
     * 更新字典
     *
     * @param sysDict 字典
     * @return Boolean
     */
    R<Boolean> updateDict(SysDict sysDict);

}
