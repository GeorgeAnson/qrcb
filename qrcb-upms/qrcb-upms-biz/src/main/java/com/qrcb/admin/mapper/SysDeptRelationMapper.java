package com.qrcb.admin.mapper;

import com.qrcb.admin.api.entity.SysDeptRelation;
import com.qrcb.common.core.data.datascope.QrcbBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 部门关系信息 Mapper <br/>
 */

@Mapper
public interface SysDeptRelationMapper extends QrcbBaseMapper<SysDeptRelation> {

    /**
     * 删除部门关系表数据
     *
     * @param id 部门ID
     */
    void deleteDeptRelationsById(Integer id);

    /**
     * 更改部分关系表数据
     *
     * @param deptRelation 部门关系信息
     */
    void updateDeptRelations(SysDeptRelation deptRelation);

}
