package com.qrcb.common.core.data.datascope;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author Anson
 * @Create 2023-10-23
 * @Description 扩展通用 Mapper，支持数据权限和批量插入 <br/>
 */

public interface QrcbBaseMapper<T> extends BaseMapper<T> {

    /**
     * 根据 entity 条件，查询全部记录
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param scope        数据权限范围
     * @return List<T>
     */
    List<T> selectListByScope(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper, DataScope scope);

    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param scope        数据权限范围
     * @return Page
     */
    <E extends IPage<T>> E selectPageByScope(E page, @Param(Constants.WRAPPER) Wrapper<T> queryWrapper,
                                             DataScope scope);

    /**
     * 根据 Wrapper 条件，查询总记录数
     *
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     * @param scope        数据权限范围
     * @return Integer
     */
    Long selectCountByScope(@Param(Constants.WRAPPER) Wrapper<T> queryWrapper, DataScope scope);

    /**
     * 批量插入 仅适用于 mysql
     * 考虑方法适用范围有限，不建议使用
     * @param entityList 实体列表
     * @return 影响行数
     */
    @Deprecated
    Integer insertBatchSomeColumn(List<T> entityList);

}
