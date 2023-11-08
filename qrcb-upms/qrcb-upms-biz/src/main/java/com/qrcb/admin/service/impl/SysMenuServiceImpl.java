package com.qrcb.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qrcb.admin.api.dto.MenuTree;
import com.qrcb.admin.api.entity.SysMenu;
import com.qrcb.admin.api.entity.SysRoleMenu;
import com.qrcb.admin.api.util.TreeUtils;
import com.qrcb.admin.api.vo.MenuVo;
import com.qrcb.admin.mapper.SysMenuMapper;
import com.qrcb.admin.mapper.SysRoleMenuMapper;
import com.qrcb.admin.service.SysMenuService;
import com.qrcb.common.core.assemble.constant.CacheConstants;
import com.qrcb.common.core.assemble.constant.CommonConstants;
import com.qrcb.common.core.assemble.constant.enums.MenuTypeEnum;
import com.qrcb.common.core.assemble.exception.CheckedException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 菜单权限表 Service 实现类 <br/>
 */

@Service
@AllArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    @Cacheable(value = CacheConstants.MENU_DETAILS, key = "#roleId", unless = "#result.isEmpty()")
    public List<MenuVo> findMenuByRoleId(Integer roleId) {
        return baseMapper.listMenusByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
    public Boolean removeMenuById(Integer id) {
        // 查询父节点为当前节点的节点
        List<SysMenu> menuList = this.list(Wrappers.<SysMenu>query().lambda()
                .eq(SysMenu::getParentId, id));
        if (CollUtil.isNotEmpty(menuList)) {
            throw new CheckedException("菜单含有下级不能删除");
        }

        sysRoleMenuMapper.delete(Wrappers.<SysRoleMenu>query().lambda()
                .eq(SysRoleMenu::getMenuId, id));
        // 删除当前菜单及其子菜单
        return this.removeById(id);
    }

    @Override
    @CacheEvict(value = CacheConstants.MENU_DETAILS, allEntries = true)
    public Boolean updateMenuById(SysMenu sysMenu) {
        return this.updateById(sysMenu);
    }

    @Override
    public List<MenuTree> treeMenu(boolean lazy, Integer parentId) {
        if (!lazy) {
            return TreeUtils.buildTree(baseMapper.selectList(Wrappers.<SysMenu>lambdaQuery()
                            .orderByAsc(SysMenu::getSort)), CommonConstants.MENU_TREE_ROOT_ID);
        }

        Integer parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
        return TreeUtils.buildTree(
                baseMapper.selectList(Wrappers.<SysMenu>lambdaQuery()
                        .eq(SysMenu::getParentId, parent)
                        .orderByAsc(SysMenu::getSort)), parent);
    }

    @Override
    public List<MenuTree> filterMenu(Set<MenuVo> voSet, String type, Integer parentId) {
        List<MenuTree> menuTreeList = voSet.stream()
                .filter(menuTypePredicate(type))
                .map(MenuTree::new)
                .sorted(Comparator.comparingInt(MenuTree::getSort))
                .collect(Collectors.toList());
        Integer parent = parentId == null ? CommonConstants.MENU_TREE_ROOT_ID : parentId;
        return TreeUtils.build(menuTreeList, parent);
    }

    /**
     * menu 类型断言
     *
     * @param type 类型
     * @return Predicate {@link MenuVo}
     */
    private Predicate<MenuVo> menuTypePredicate(String type) {
        return vo -> {
            if (MenuTypeEnum.TOP_MENU.getDescription().equals(type)) {
                return MenuTypeEnum.TOP_MENU.getType().equals(vo.getType());
            }
            // 其他查询 左侧 + 顶部
            return !MenuTypeEnum.BUTTON.getType().equals(vo.getType());
        };
    }

}
