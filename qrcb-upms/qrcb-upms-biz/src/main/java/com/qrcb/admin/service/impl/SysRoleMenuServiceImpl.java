package com.qrcb.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.qrcb.admin.api.entity.SysRoleMenu;
import com.qrcb.admin.mapper.SysRoleMenuMapper;
import com.qrcb.admin.service.SysRoleMenuService;
import com.qrcb.common.core.assemble.constant.CacheConstants;
import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 角色菜单表 Service 实现类 <br/>
 */

@Service
@AllArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    private final CacheManager cacheManager;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CacheConstants.MENU_DETAILS, key = "#roleId")
    public Boolean saveRoleMenus(String role, Integer roleId, String menuIds) {
        this.remove(Wrappers.<SysRoleMenu>query().lambda().eq(SysRoleMenu::getRoleId, roleId));

        if (StrUtil.isBlank(menuIds)) {
            return Boolean.TRUE;
        }
        List<SysRoleMenu> roleMenuList = Arrays.stream(menuIds.split(",")).map(menuId -> {
            SysRoleMenu roleMenu = new SysRoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(Integer.valueOf(menuId));
            return roleMenu;
        }).collect(Collectors.toList());

        // 清空 userinfo
        cacheManager.getCache(CacheConstants.USER_DETAILS).clear();

        return SqlHelper.retBool(baseMapper.insertBatchSomeColumn(roleMenuList));
    }

}
