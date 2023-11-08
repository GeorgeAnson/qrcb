package com.qrcb.admin.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qrcb.admin.api.dto.DeptTree;
import com.qrcb.admin.api.entity.SysDept;
import com.qrcb.admin.api.entity.SysDeptRelation;
import com.qrcb.admin.service.SysDeptRelationService;
import com.qrcb.admin.service.SysDeptService;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.log.annotation.SysLog;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 部门管理模块 <br/>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/dept")
@Api(value = "dept", tags = "部门管理模块")
public class SysDeptController {

    private final SysDeptRelationService relationService;

    private final SysDeptService sysDeptService;

    /**
     * 通过ID查询
     *
     * @param id ID
     * @return {@link SysDept}
     */
    @GetMapping("/{id}")
    public R<SysDept> getById(@PathVariable Integer id) {
        return R.ok(sysDeptService.getById(id));
    }

    /**
     * 返回树形菜单集合
     *
     * @return {@link DeptTree} List 树形菜单
     */
    @GetMapping(value = "/tree")
    public R<List<DeptTree>> getTree() {
        return R.ok(sysDeptService.selectTree());
    }

    /**
     * 添加
     *
     * @param sysDept 实体
     * @return {@link Boolean}
     */
    @SysLog("添加部门")
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys_dept_add')")
    public R<Boolean> save(@Valid @RequestBody SysDept sysDept) {
        return R.ok(sysDeptService.saveDept(sysDept));
    }

    /**
     * 删除
     *
     * @param id ID
     * @return {@link Boolean}
     */
    @SysLog("删除部门")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_dept_del')")
    public R<Boolean> removeById(@PathVariable Integer id) {
        return R.ok(sysDeptService.removeDeptById(id));
    }

    /**
     * 编辑
     *
     * @param sysDept 实体
     * @return {@link Boolean}
     */
    @SysLog("编辑部门")
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys_dept_edit')")
    public R<Boolean> update(@Valid @RequestBody SysDept sysDept) {
        sysDept.setUpdateTime(LocalDateTime.now());
        return R.ok(sysDeptService.updateDeptById(sysDept));
    }

    /**
     * 查收子级列表
     *
     * @return {@link SysDeptRelation} List返回子级
     */
    @GetMapping(value = "/getDescendantList/{deptId}")
    public R<List<SysDeptRelation>> getDescendantList(@PathVariable Integer deptId) {
        return R.ok(relationService.list(Wrappers.<SysDeptRelation>lambdaQuery()
                .eq(SysDeptRelation::getAncestor, deptId)));
    }

}
