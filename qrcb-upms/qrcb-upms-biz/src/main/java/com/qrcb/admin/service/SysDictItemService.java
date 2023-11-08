package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.entity.SysDictItem;
import com.qrcb.common.core.assemble.util.R;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 字典项 Service 接口 <br/>
 */

public interface SysDictItemService extends IService<SysDictItem> {

    /**
     * 删除字典项
     *
     * @param id 字典项ID
     * @return {@link Boolean}
     */
    R<Boolean> removeDictItem(Integer id);

    /**
     * 更新字典项
     *
     * @param item 字典项
     * @return {@link Boolean}
     */
    R<Boolean> updateDictItem(SysDictItem item);

}
