package com.qrcb.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.qrcb.admin.api.entity.SysDict;
import com.qrcb.admin.api.entity.SysDictItem;
import com.qrcb.admin.mapper.SysDictItemMapper;
import com.qrcb.admin.mapper.SysDictMapper;
import com.qrcb.admin.service.SysDictService;
import com.qrcb.common.core.assemble.constant.CacheConstants;
import com.qrcb.common.core.assemble.constant.enums.DictTypeEnum;
import com.qrcb.common.core.assemble.util.R;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 字典表 服务实现类 <br/>
 */

@Service
@AllArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {

    private final SysDictItemMapper dictItemMapper;

    @Override
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public R<Boolean> removeDict(Integer id) {
        SysDict dict = this.getById(id);
        // 系统内置
        if (DictTypeEnum.SYSTEM.getType().equals(dict.getSystem())) {
            return R.failed("系统内置字典不能删除");
        }

        baseMapper.deleteById(id);
        return R.ok(SqlHelper.retBool(dictItemMapper.delete(Wrappers.<SysDictItem>lambdaQuery()
                .eq(SysDictItem::getDictId, id))));
    }

    @Override
    public R<Boolean> updateDict(SysDict dict) {
        SysDict sysDict = this.getById(dict.getId());
        // 系统内置
        if (DictTypeEnum.SYSTEM.getType().equals(sysDict.getSystem())) {
            return R.failed("系统内置字典不能修改");
        }
        return R.ok(this.updateById(dict));
    }

}