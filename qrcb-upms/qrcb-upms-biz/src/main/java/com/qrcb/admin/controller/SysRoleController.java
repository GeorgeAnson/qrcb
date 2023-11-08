package com.qrcb.admin.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.admin.api.entity.SysRole;
import com.qrcb.admin.api.vo.RoleVo;
import com.qrcb.admin.service.SysRoleService;
import com.qrcb.common.core.assemble.constant.CacheConstants;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.log.annotation.SysLog;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 角色管理模块 <br/>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/role")
@Api(value = "role", tags = "角色管理模块")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    /**
     * 通过ID查询角色信息
     *
     * @param id ID
     * @return {@link SysRole} 角色信息
     */
    @GetMapping("/{id}")
    public R<SysRole> getById(@PathVariable Integer id) {
        return R.ok(sysRoleService.getById(id));
    }

    /**
     * 添加角色
     *
     * @param sysRole 角色信息
     * @return {@link Boolean} success、false
     */
    @SysLog("添加角色")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_role_add')")
    @CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
    public R<Boolean> save(@Valid @RequestBody SysRole sysRole) {
        return R.ok(sysRoleService.save(sysRole));
    }

    /**
     * 修改角色
     *
     * @param sysRole 角色信息
     * @return {@link Boolean} success/false
     */
    @SysLog("修改角色")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_role_edit')")
    @CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
    public R<Boolean> update(@Valid @RequestBody SysRole sysRole) {
        return R.ok(sysRoleService.updateById(sysRole));
    }

    /**
     * 删除角色
     *
     * @param id id
     * @return {@link Boolean}
     */
    @SysLog("删除角色")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_role_del')")
    @CacheEvict(value = CacheConstants.ROLE_DETAILS, allEntries = true)
    public R<Boolean> removeById(@PathVariable Integer id) {
        return R.ok(sysRoleService.removeRoleById(id));
    }

    /**
     * 获取角色列表
     *
     * @return {@link SysRole} List 角色列表
     */
    @GetMapping("/list")
    public R<List<SysRole>> listRoles() {
        return R.ok(sysRoleService.list(Wrappers.emptyWrapper()));
    }

    /**
     * 分页查询角色信息
     *
     * @param page 分页对象
     * @param role 查询条件
     * @return {@link SysRole} Page 分页对象
     */
    @GetMapping("/page")
    public R<IPage<SysRole>> getRolePage(Page<SysRole> page, SysRole role) {
        return R.ok(sysRoleService.page(page, Wrappers.query(role)));
    }

    /**
     * 更新角色菜单
     *
     * @param roleVo 角色对象
     * @return {@link Boolean} success、false
     */
    @SysLog("更新角色菜单")
    @PutMapping("/menu")
    @PreAuthorize("@pms.hasPermission('sys_role_perm')")
    public R<Boolean> saveRoleMenus(@RequestBody RoleVo roleVo) {
        return R.ok(sysRoleService.updateRoleMenus(roleVo));
    }

    /**
     * 通过角色ID 查询角色列表
     *
     * @param roleIdList 角色ID
     * @return {@link SysRole} List
     */
    @PostMapping("/getRoleList")
    public R<List<SysRole>> getRoleList(@RequestBody List<Integer> roleIdList) {
        return R.ok(sysRoleService.findRolesByRoleIds(roleIdList, CollUtil.join(roleIdList, StrUtil.UNDERLINE)));
    }

}
