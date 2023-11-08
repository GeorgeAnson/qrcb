package com.qrcb.admin.service.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qrcb.admin.api.dto.UserDto;
import com.qrcb.admin.api.dto.UserInfo;
import com.qrcb.admin.api.entity.SysDept;
import com.qrcb.admin.api.entity.SysRole;
import com.qrcb.admin.api.entity.SysUser;
import com.qrcb.admin.api.entity.SysUserRole;
import com.qrcb.admin.api.vo.MenuVo;
import com.qrcb.admin.api.vo.UserVo;
import com.qrcb.admin.mapper.SysUserMapper;
import com.qrcb.admin.service.*;
import com.qrcb.common.core.assemble.constant.CacheConstants;
import com.qrcb.common.core.assemble.constant.CommonConstants;
import com.qrcb.common.core.assemble.exception.CheckedException;
import com.qrcb.common.core.data.datascope.DataScope;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 用户表  Service 实现类 <br/>
 */

@Slf4j
@Service
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private final SysMenuService sysMenuService;

    private final SysRoleService sysRoleService;

    private final SysDeptService sysDeptService;

    private final SysUserRoleService sysUserRoleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveUser(UserDto userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
        sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        baseMapper.insert(sysUser);
        List<SysUserRole> userRoleList = userDto.getRole().stream()
                .map(roleId -> {
                    SysUserRole userRole = new SysUserRole();
                    userRole.setUserId(sysUser.getUserId());
                    userRole.setRoleId(roleId);
                    return userRole;
                }).collect(Collectors.toList());
        return sysUserRoleService.saveBatch(userRoleList);
    }

    @Override
    public UserInfo findUserInfo(SysUser sysUser) {
        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        // 设置角色列表 （ID）
        List<Integer> roleIds = sysRoleService.findRolesByUserId(sysUser.getUserId()).stream()
                .map(SysRole::getRoleId)
                .collect(Collectors.toList());
        userInfo.setRoles(ArrayUtil.toArray(roleIds, Integer.class));

        // 设置权限列表（menu.permission）
        Set<String> permissions = new HashSet<>();
        roleIds.forEach(roleId -> {
            List<String> permissionList = sysMenuService.findMenuByRoleId(roleId).stream()
                    .filter(menuVo -> StringUtil.isNotEmpty(menuVo.getPermission()))
                    .map(MenuVo::getPermission)
                    .collect(Collectors.toList());
            permissions.addAll(permissionList);
        });
        userInfo.setPermissions(ArrayUtil.toArray(permissions, String.class));
        return userInfo;
    }

    @Override
    public IPage<UserVo> getUsersWithRolePage(Page page, UserDto userDto) {
        return baseMapper.getUserVosPage(page, userDto, new DataScope());
    }

    @Override
    public UserVo selectUserVoById(Integer id) {
        return baseMapper.getUserVoById(id);
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#sysUser.username")
    public Boolean deleteUserById(SysUser sysUser) {
        sysUserRoleService.deleteByUserId(sysUser.getUserId());
        this.removeById(sysUser.getUserId());
        return Boolean.TRUE;
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public Boolean updateUserInfo(UserDto userDto) {
        UserVo userVO = baseMapper.getUserVoByUsername(userDto.getUsername());
        if (!ENCODER.matches(userDto.getPassword(), userVO.getPassword())) {
            log.info("原密码错误，修改个人信息失败:{}", userDto.getUsername());
            throw new CheckedException("原密码错误，修改个人信息失败");
        }

        SysUser sysUser = new SysUser();
        if (StrUtil.isNotBlank(userDto.getNewPassword())) {
            sysUser.setPassword(ENCODER.encode(userDto.getNewPassword()));
        }
        sysUser.setPhone(userDto.getPhone());
        sysUser.setUserId(userVO.getUserId());
        sysUser.setAvatar(userDto.getAvatar());
        return this.updateById(sysUser);
    }

    @Override
    @CacheEvict(value = CacheConstants.USER_DETAILS, key = "#userDto.username")
    public Boolean updateUser(UserDto userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setUpdateTime(LocalDateTime.now());

        if (StrUtil.isNotBlank(userDto.getPassword())) {
            sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        }
        this.updateById(sysUser);

        sysUserRoleService.remove(Wrappers.<SysUserRole>update().lambda()
                .eq(SysUserRole::getUserId, userDto.getUserId()));
        userDto.getRole().forEach(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(sysUser.getUserId());
            userRole.setRoleId(roleId);
            userRole.insert();
        });
        return Boolean.TRUE;
    }

    @Override
    public List<SysUser> listAncestorUsers(String username) {
        SysUser sysUser = this.getOne(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getUsername, username));

        SysDept sysDept = sysDeptService.getById(sysUser.getDeptId());
        if (sysDept == null) {
            return null;
        }

        Integer parentId = sysDept.getParentId();
        return this.list(Wrappers.<SysUser>query().lambda()
                .eq(SysUser::getDeptId, parentId));
    }

}
