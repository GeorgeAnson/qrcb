package com.qrcb.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qrcb.admin.api.dto.DeptTree;
import com.qrcb.admin.api.entity.SysDept;
import com.qrcb.admin.api.entity.SysDeptRelation;
import com.qrcb.admin.api.util.TreeUtils;
import com.qrcb.admin.mapper.SysDeptMapper;
import com.qrcb.admin.service.SysDeptRelationService;
import com.qrcb.admin.service.SysDeptService;
import com.qrcb.common.core.data.datascope.DataScope;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 部门管理 服务实现类 <br/>
 */

@Service
@AllArgsConstructor
public class SysDeptServiceImpl extends ServiceImpl<SysDeptMapper, SysDept> implements SysDeptService {

    private final SysDeptRelationService sysDeptRelationService;

    private final SysDeptMapper deptMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveDept(SysDept dept) {
        SysDept sysDept = new SysDept();
        BeanUtils.copyProperties(dept, sysDept);
        this.save(sysDept);
        sysDeptRelationService.insertDeptRelation(sysDept);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeDeptById(Integer id) {
        // 级联删除部门
        List<Integer> idList = sysDeptRelationService
                .list(Wrappers.<SysDeptRelation>query().lambda().eq(SysDeptRelation::getAncestor, id)).stream()
                .map(SysDeptRelation::getDescendant).collect(Collectors.toList());

        if (CollUtil.isNotEmpty(idList)) {
            this.removeByIds(idList);
        }

        // 删除部门级联关系
        sysDeptRelationService.deleteAllDeptRelation(id);
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateDeptById(SysDept sysDept) {
        // 更新部门状态
        this.updateById(sysDept);
        // 更新部门关系
        SysDeptRelation relation = new SysDeptRelation();
        relation.setAncestor(sysDept.getParentId());
        relation.setDescendant(sysDept.getDeptId());
        sysDeptRelationService.updateDeptRelation(relation);
        return Boolean.TRUE;
    }

    @Override
    public List<DeptTree> selectTree() {
        // 查询全部部门
        List<SysDept> deptAllList = deptMapper.selectList(Wrappers.emptyWrapper());
        // 查询数据权限内部门
        List<Integer> deptOwnIdList = deptMapper.selectListByScope(Wrappers.emptyWrapper(), new DataScope()).stream()
                .map(SysDept::getDeptId).collect(Collectors.toList());

        // 权限内部门
        List<DeptTree> collect = deptAllList.stream().filter(dept -> dept.getDeptId().intValue() != dept.getParentId())
                .sorted(Comparator.comparingInt(SysDept::getSort)).map(dept -> {
                    DeptTree node = new DeptTree();
                    node.setId(dept.getDeptId());
                    node.setParentId(dept.getParentId());
                    node.setName(dept.getName());

                    // 有权限不返回标识
                    if (deptOwnIdList.contains(dept.getDeptId())) {
                        node.setIsLock(Boolean.FALSE);
                    }
                    return node;
                }).collect(Collectors.toList());
        return TreeUtils.build(collect, 0);
    }

}