package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.entity.SysDept;
import com.qrcb.admin.api.entity.SysDeptRelation;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 部门关系信息 服务类 <br/>
 */

public interface SysDeptRelationService extends IService<SysDeptRelation> {

    /**
     * 新建部门关系
     *
     * @param sysDept 部门
     */
    void insertDeptRelation(SysDept sysDept);

    /**
     * 通过ID删除部门关系
     *
     * @param id 部门ID
     */
    void deleteAllDeptRelation(Integer id);

    /**
     * 更新部门关系
     *
     * @param relation 部门关系信息
     */
    void updateDeptRelation(SysDeptRelation relation);

}
