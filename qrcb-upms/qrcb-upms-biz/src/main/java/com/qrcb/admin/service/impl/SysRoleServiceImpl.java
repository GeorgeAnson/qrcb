package com.qrcb.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qrcb.admin.api.entity.SysRole;
import com.qrcb.admin.api.entity.SysRoleMenu;
import com.qrcb.admin.api.vo.RoleVo;
import com.qrcb.admin.mapper.SysRoleMapper;
import com.qrcb.admin.service.SysRoleMenuService;
import com.qrcb.admin.service.SysRoleService;
import com.qrcb.common.core.assemble.constant.CacheConstants;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 角色表 Service 实现类 <br/>
 */

@Service
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private SysRoleMenuService roleMenuService;

    @Override
    public List<SysRole> findRolesByUserId(Integer userId) {
        return baseMapper.listRolesByUserId(userId);
    }

    @Override
    @Cacheable(value = CacheConstants.ROLE_DETAILS, key = "#key")
    public List<SysRole> findRolesByRoleIds(List<Integer> roleIdList, String key) {
        return baseMapper.selectBatchIds(roleIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeRoleById(Integer id) {
        roleMenuService.remove(Wrappers.<SysRoleMenu>update().lambda().eq(SysRoleMenu::getRoleId, id));
        return this.removeById(id);
    }

    @Override
    public Boolean updateRoleMenus(RoleVo roleVo) {
        SysRole sysRole = baseMapper.selectById(roleVo.getRoleId());
        return roleMenuService.saveRoleMenus(sysRole.getRoleCode(), roleVo.getRoleId(), roleVo.getMenuIds());
    }

}