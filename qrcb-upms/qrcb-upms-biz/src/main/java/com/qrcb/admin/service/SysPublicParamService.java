package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.entity.SysPublicParam;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 公共参数配置 Service 接口 <br/>
 */

public interface SysPublicParamService extends IService<SysPublicParam> {

    /**
     * 通过key查询公共参数指定值
     *
     * @param publicKey 参数 key
     * @return {@link String}
     */
    String getSysPublicParamKeyToValue(String publicKey);

    /**
     * 更新参数
     *
     * @param sysPublicParam 参数
     * @return {@link Boolean}
     */
    Boolean updateParam(SysPublicParam sysPublicParam);

    /**
     * 删除参数
     *
     * @param publicId 主键ID
     * @return {@link Boolean}
     */
    Boolean removeParam(Long publicId);

}