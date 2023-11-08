package com.qrcb.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.admin.api.entity.SysDict;
import com.qrcb.admin.api.entity.SysDictItem;
import com.qrcb.admin.service.SysDictItemService;
import com.qrcb.admin.service.SysDictService;
import com.qrcb.common.core.assemble.constant.CacheConstants;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.log.annotation.SysLog;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 字典管理模块 <br/>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/dict")
@Api(value = "dict", tags = "字典管理模块")
public class SysDictController {

    private final SysDictService sysDictService;

    private final SysDictItemService sysDictItemService;

    /**
     * 通过ID查询字典信息
     *
     * @param id ID
     * @return {@link SysDict} 字典信息
     */
    @GetMapping("/{id}")
    public R<SysDict> getById(@PathVariable Integer id) {
        return R.ok(sysDictService.getById(id));
    }

    /**
     * 分页查询字典信息
     *
     * @param page 分页对象
     * @return {@link SysDict} Page 分页对象
     */
    @GetMapping("/page")
    public R<IPage<SysDict>> getDictPage(Page<SysDict> page, SysDict sysDict) {
        return R.ok(sysDictService.page(page, Wrappers.query(sysDict)));
    }

    /**
     * 通过字典类型查找字典
     *
     * @param type 类型
     * @return {@link SysDictItem} List 同类型字典
     */
    @GetMapping("/type/{type}")
    @Cacheable(value = CacheConstants.DICT_DETAILS, key = "#type", unless = "#result.data.isEmpty()")
    public R<List<SysDictItem>> getDictByType(@PathVariable String type) {
        return R.ok(sysDictItemService.list(Wrappers.<SysDictItem>query().lambda().eq(SysDictItem::getType, type)));
    }

    /**
     * 添加字典
     *
     * @param sysDict 字典信息
     * @return {@link Boolean}
     */
    @SysLog("添加字典")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_dict_add')")
    public R<Boolean> save(@Valid @RequestBody SysDict sysDict) {
        return R.ok(sysDictService.save(sysDict));
    }

    /**
     * 删除字典，并且清除字典缓存
     *
     * @param id ID
     * @return {@link Boolean}
     */
    @SysLog("删除字典")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_dict_del')")
    public R<Boolean> removeById(@PathVariable Integer id) {
        return sysDictService.removeDict(id);
    }

    /**
     * 修改字典
     *
     * @param sysDict 字典信息
     * @return {@link Boolean}
     */
    @PutMapping
    @SysLog("修改字典")
    @PreAuthorize("@pms.hasPermission('sys_dict_edit')")
    public R<Boolean> updateById(@Valid @RequestBody SysDict sysDict) {
        return sysDictService.updateDict(sysDict);
    }

    /**
     * 分页查询
     *
     * @param page        分页对象
     * @param sysDictItem 字典项
     * @return {@link SysDictItem} Page
     */
    @GetMapping("/item/page")
    public R<Page<SysDictItem>> getSysDictItemPage(Page<SysDictItem> page, SysDictItem sysDictItem) {
        return R.ok(sysDictItemService.page(page, Wrappers.query(sysDictItem)));
    }

    /**
     * 通过id查询字典项
     *
     * @param id id
     * @return {@link SysDictItem}
     */
    @GetMapping("/item/{id}")
    public R<SysDictItem> getDictItemById(@PathVariable("id") Integer id) {
        return R.ok(sysDictItemService.getById(id));
    }

    /**
     * 新增字典项
     *
     * @param sysDictItem 字典项
     * @return {@link Boolean}
     */
    @SysLog("新增字典项")
    @PostMapping("/item")
    @CacheEvict(value = CacheConstants.DICT_DETAILS, allEntries = true)
    public R<Boolean> save(@RequestBody SysDictItem sysDictItem) {
        return R.ok(sysDictItemService.save(sysDictItem));
    }

    /**
     * 修改字典项
     *
     * @param sysDictItem 字典项
     * @return {@link Boolean}
     */
    @SysLog("修改字典项")
    @PutMapping("/item")
    public R<Boolean> updateById(@RequestBody SysDictItem sysDictItem) {
        return sysDictItemService.updateDictItem(sysDictItem);
    }

    /**
     * 通过id删除字典项
     *
     * @param id id
     * @return {@link Boolean}
     */
    @SysLog("删除字典项")
    @DeleteMapping("/item/{id}")
    public R<Boolean> removeDictItemById(@PathVariable Integer id) {
        return sysDictItemService.removeDictItem(id);
    }

}
