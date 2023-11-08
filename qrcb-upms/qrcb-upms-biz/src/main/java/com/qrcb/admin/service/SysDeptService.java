package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.dto.DeptTree;
import com.qrcb.admin.api.entity.SysDept;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 部门管理 服务类 <br/>
 */

public interface SysDeptService extends IService<SysDept> {

    /**
     * 查询部门树菜单
     *
     * @return {@link DeptTree} List
     */
    List<DeptTree> selectTree();

    /**
     * 添加信息部门
     *
     * @param sysDept 部门信息
     * @return {@link Boolean}
     */
    Boolean saveDept(SysDept sysDept);

    /**
     * 删除部门
     *
     * @param id 部门 ID
     * @return {@link Boolean}
     */
    Boolean removeDeptById(Integer id);

    /**
     * 更新部门
     *
     * @param sysDept 部门信息
     * @return {@link Boolean}
     */
    Boolean updateDeptById(SysDept sysDept);

}
