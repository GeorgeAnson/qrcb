package com.qrcb.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.admin.api.dto.UserDto;
import com.qrcb.admin.api.dto.UserInfo;
import com.qrcb.admin.api.entity.SysUser;
import com.qrcb.admin.api.vo.UserVo;
import com.qrcb.admin.service.SysUserService;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.log.annotation.SysLog;
import com.qrcb.common.core.security.annotation.Inner;
import com.qrcb.common.core.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 用户管理模块 <br/>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/user")
@Api(value = "user", tags = "用户管理模块")
public class SysUserController {

    private final SysUserService userService;

    /**
     * 获取指定用户全部信息
     *
     * @return {@link UserInfo} 用户信息
     */
    @Inner
    @GetMapping("/info/{username}")
    public R<UserInfo> info(@PathVariable String username) {
        SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (user == null) {
            return R.failed(null, String.format("用户信息为空 %s", username));
        }
        return R.ok(userService.findUserInfo(user));
    }

    /**
     * 获取当前用户全部信息
     *
     * @return {@link UserInfo} 用户信息
     */
    @GetMapping(value = {"/info"})
    public R<UserInfo> info() {
        String username = SecurityUtils.getUser().getUsername();
        SysUser user = userService.getOne(Wrappers.<SysUser>query().lambda().eq(SysUser::getUsername, username));
        if (user == null) {
            return R.failed(null, "获取当前用户信息失败");
        }
        return R.ok(userService.findUserInfo(user));
    }

    /**
     * 通过ID查询用户信息
     *
     * @param id ID
     * @return {@link UserVo} 用户信息
     */
    @GetMapping("/{id}")
    public R<UserVo> user(@PathVariable Integer id) {
        return R.ok(userService.selectUserVoById(id));
    }

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return {@link SysUser}
     */
    @GetMapping("/details/{username}")
    public R<SysUser> user(@PathVariable String username) {
        SysUser condition = new SysUser();
        condition.setUsername(username);
        return R.ok(userService.getOne(new QueryWrapper<>(condition)));
    }

    /**
     * 删除用户信息
     *
     * @param id ID
     * @return {@link Boolean}
     */
    @SysLog("删除用户信息")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_user_del')")
    @ApiOperation(value = "删除用户", notes = "根据ID删除用户")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int", paramType = "path")
    public R<Boolean> userDel(@PathVariable Integer id) {
        SysUser sysUser = userService.getById(id);
        return R.ok(userService.deleteUserById(sysUser));
    }

    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return {@link Boolean} success/false
     */
    @SysLog("添加用户")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_user_add')")
    public R<Boolean> user(@RequestBody UserDto userDto) {
        return R.ok(userService.saveUser(userDto));
    }

    /**
     * 更新用户信息
     *
     * @param userDto 用户信息
     * @return {@link Boolean}
     */
    @SysLog("更新用户信息")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_user_edit')")
    public R<Boolean> updateUser(@Valid @RequestBody UserDto userDto) {
        return R.ok(userService.updateUser(userDto));
    }

    /**
     * 分页查询用户
     *
     * @param page    参数集
     * @param userDTO 查询参数列表
     * @return {@link UserVo} Page 用户集合
     */
    @GetMapping("/page")
    public R<IPage<UserVo>> getUserPage(Page page, UserDto userDTO) {
        return R.ok(userService.getUsersWithRolePage(page, userDTO));
    }

    /**
     * 修改个人信息
     *
     * @param userDto userDto
     * @return {@link Boolean} success/false
     */
    @SysLog("修改个人信息")
    @PutMapping("/edit")
    public R<Boolean> updateUserInfo(@Valid @RequestBody UserDto userDto) {
        return R.ok(userService.updateUserInfo(userDto));
    }

    /**
     * @param username 用户名称
     * @return {@link SysUser} List 上级部门用户列表
     */
    @GetMapping("/ancestor/{username}")
    public R<List<SysUser>> listAncestorUsers(@PathVariable String username) {
        return R.ok(userService.listAncestorUsers(username));
    }

}
