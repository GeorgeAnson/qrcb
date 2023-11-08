package com.qrcb.admin.controller;

import com.qrcb.admin.api.dto.MenuTree;
import com.qrcb.admin.api.entity.SysMenu;
import com.qrcb.admin.api.vo.MenuVo;
import com.qrcb.admin.service.SysMenuService;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.log.annotation.SysLog;
import com.qrcb.common.core.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 菜单管理模块 <br/>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/menu")
@Api(value = "menu", tags = "菜单管理模块")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    /**
     * 返回当前用户的树形菜单集合
     *
     * @param type     类型
     * @param parentId 父节点ID
     * @return {@link MenuTree} List 当前用户的树形菜单
     */
    @GetMapping
    public R<List<MenuTree>> getUserMenu(String type, Integer parentId) {
        // 获取符合条件的菜单
        Set<MenuVo> all = new HashSet<>();
        SecurityUtils.getRoles().forEach(roleId -> all.addAll(sysMenuService.findMenuByRoleId(roleId)));
        return R.ok(sysMenuService.filterMenu(all, type, parentId));
    }

    /**
     * 返回树形菜单集合
     *
     * @param lazy     是否是懒加载
     * @param parentId 父节点ID
     * @return {@link MenuTree} List 树形菜单
     */
    @GetMapping(value = "/tree")
    public R<List<MenuTree>> getTree(boolean lazy, Integer parentId) {
        return R.ok(sysMenuService.treeMenu(lazy, parentId));
    }

    /**
     * 返回角色的菜单集合
     *
     * @param roleId 角色ID
     * @return {@link Integer} List 属性集合
     */
    @GetMapping("/tree/{roleId}")
    public R<List<Integer>> getRoleTree(@PathVariable Integer roleId) {
        return R.ok(sysMenuService.findMenuByRoleId(roleId).stream()
                .map(MenuVo::getMenuId)
                .collect(Collectors.toList()));
    }

    /**
     * 通过ID查询菜单的详细信息
     *
     * @param id 菜单ID
     * @return {@link SysMenu} 菜单详细信息
     */
    @GetMapping("/{id}")
    public R<SysMenu> getById(@PathVariable Integer id) {
        return R.ok(sysMenuService.getById(id));
    }

    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return {@link Boolean} success/false
     */
    @SysLog("新增菜单")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_menu_add')")
    public R<Boolean> save(@Valid @RequestBody SysMenu sysMenu) {
        return R.ok(sysMenuService.save(sysMenu));
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return {@link Boolean} success/false
     */
    @SysLog("删除菜单")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_menu_del')")
    public R<Boolean> removeById(@PathVariable Integer id) {
        return R.ok(sysMenuService.removeMenuById(id));
    }

    /**
     * 更新菜单
     *
     * @param sysMenu 菜单
     * @return {@link Boolean}
     */
    @SysLog("更新菜单")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_menu_edit')")
    public R<Boolean> update(@Valid @RequestBody SysMenu sysMenu) {
        return R.ok(sysMenuService.updateMenuById(sysMenu));
    }

}
