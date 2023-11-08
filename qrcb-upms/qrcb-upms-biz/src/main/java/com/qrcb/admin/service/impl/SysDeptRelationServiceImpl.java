package com.qrcb.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qrcb.admin.api.entity.SysDept;
import com.qrcb.admin.api.entity.SysDeptRelation;
import com.qrcb.admin.mapper.SysDeptRelationMapper;
import com.qrcb.admin.service.SysDeptRelationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 部门关系信息 服务实现类 <br/>
 */

@Service
@AllArgsConstructor
public class SysDeptRelationServiceImpl extends ServiceImpl<SysDeptRelationMapper, SysDeptRelation>
        implements SysDeptRelationService {

    private final SysDeptRelationMapper sysDeptRelationMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertDeptRelation(SysDept sysDept) {
        // 保存部门上下级关系表
        List<SysDeptRelation> relationList = sysDeptRelationMapper.selectList(
                        Wrappers.<SysDeptRelation>query().lambda().eq(SysDeptRelation::getDescendant, sysDept.getParentId()))
                .stream().map(relation -> {
                    relation.setDescendant(sysDept.getDeptId());
                    return relation;
                }).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(relationList)) {
            baseMapper.insertBatchSomeColumn(relationList);
        }

        // 自己也要维护到关系表中
        SysDeptRelation own = new SysDeptRelation();
        own.setDescendant(sysDept.getDeptId());
        own.setAncestor(sysDept.getDeptId());
        sysDeptRelationMapper.insert(own);
    }

    @Override
    public void deleteAllDeptRelation(Integer id) {
        baseMapper.deleteDeptRelationsById(id);
    }

    @Override
    public void updateDeptRelation(SysDeptRelation relation) {
        baseMapper.updateDeptRelations(relation);
    }

}