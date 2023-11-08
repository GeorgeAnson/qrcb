package com.qrcb.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.admin.api.dto.UserInfo;
import com.qrcb.admin.api.entity.SysSocialDetails;
import com.qrcb.admin.service.SysSocialDetailsService;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.log.annotation.SysLog;
import com.qrcb.common.core.security.annotation.Inner;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 三方账号管理模块 <br/>
 */

@RestController
@RequestMapping("/social")
@AllArgsConstructor
@Api(value = "social", tags = "三方账号管理模块")
public class SysSocialDetailsController {

    private final SysSocialDetailsService sysSocialDetailsService;

    /**
     * 社交登录账户简单分页查询
     *
     * @param page             分页对象
     * @param sysSocialDetails 社交登录
     * @return {@link SysSocialDetails} Page
     */
    @GetMapping("/page")
    public R<IPage<SysSocialDetails>> getSocialDetailsPage(Page<SysSocialDetails> page, SysSocialDetails sysSocialDetails) {
        return R.ok(sysSocialDetailsService.page(page, Wrappers.query(sysSocialDetails)));
    }

    /**
     * 信息
     *
     * @param type 类型
     * @return {@link SysSocialDetails}
     */
    @GetMapping("/{type}")
    public R<List<SysSocialDetails>> getByType(@PathVariable("type") String type) {
        return R.ok(sysSocialDetailsService.list(Wrappers.<SysSocialDetails>lambdaQuery()
                .eq(SysSocialDetails::getType, type)));
    }

    /**
     * 保存
     *
     * @param sysSocialDetails 三方社交信息
     * @return {@link Boolean}
     */
    @SysLog("保存三方信息")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_social_details_add')")
    public R<Boolean> save(@Valid @RequestBody SysSocialDetails sysSocialDetails) {
        return R.ok(sysSocialDetailsService.save(sysSocialDetails));
    }

    /**
     * 修改
     *
     * @param sysSocialDetails 三方社交信息
     * @return {@link Boolean}
     */
    @SysLog("修改三方信息")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_social_details_edit')")
    public R<Boolean> updateById(@Valid @RequestBody SysSocialDetails sysSocialDetails) {
        sysSocialDetailsService.updateById(sysSocialDetails);
        return R.ok(Boolean.TRUE);
    }

    /**
     * 删除
     *
     * @param id id
     * @return {@link Boolean}
     */
    @SysLog("删除三方信息")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_social_details_del')")
    public R<Boolean> removeById(@PathVariable Integer id) {
        return R.ok(sysSocialDetailsService.removeById(id));
    }

    /**
     * 通过社交账号、手机号查询用户、角色信息
     *
     * @param inStr appid@code
     * @return {@link UserInfo}
     */
    @Inner
    @GetMapping("/info/{inStr}")
    public R<UserInfo> getUserInfo(@PathVariable String inStr) {
        return R.ok(sysSocialDetailsService.getUserInfo(inStr));
    }

    /**
     * 绑定社交账号
     *
     * @param state 类型
     * @param code  code
     * @return {@link Boolean}
     */
    @PostMapping("/bind")
    public R<Boolean> bindSocial(String state, String code) {
        return R.ok(sysSocialDetailsService.bindSocial(state, code));
    }

}
