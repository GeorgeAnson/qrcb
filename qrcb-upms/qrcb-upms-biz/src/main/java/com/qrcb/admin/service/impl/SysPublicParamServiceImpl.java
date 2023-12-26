package com.qrcb.admin.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.qrcb.admin.api.entity.SysPublicParam;
import com.qrcb.admin.mapper.SysPublicParamMapper;
import com.qrcb.admin.service.SysPublicParamService;
import com.qrcb.common.core.assemble.constant.CacheConstants;
import com.qrcb.common.core.assemble.constant.enums.DictTypeEnum;
import com.qrcb.common.core.assemble.exception.CheckedException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 公共参数配置 Service 实现类 <br/>
 */

@Service
@AllArgsConstructor
public class SysPublicParamServiceImpl extends ServiceImpl<SysPublicParamMapper, SysPublicParam>
        implements SysPublicParamService {

    @Override
    @Cacheable(value = CacheConstants.PARAMS_DETAILS, key = "#publicKey", unless = "#result == null ")
    public String getSysPublicParamKeyToValue(String publicKey) {
        SysPublicParam sysPublicParam = this.baseMapper
                .selectOne(Wrappers.<SysPublicParam>lambdaQuery().eq(SysPublicParam::getPublicKey, publicKey));

        if (sysPublicParam != null) {
            return sysPublicParam.getPublicValue();
        }
        return null;
    }

    @Override
    public Boolean saveParam(SysPublicParam sysPublicParam) {
        Long exist = this.baseMapper.selectCount(Wrappers.<SysPublicParam>query().lambda()
                .eq(SysPublicParam::getPublicKey, sysPublicParam.getPublicKey()));
        if(SqlHelper.retBool(exist)){
            throw new CheckedException("参数键不能重复");
        }
        return this.save(sysPublicParam);
    }

    @Override
    @CacheEvict(value = CacheConstants.PARAMS_DETAILS, key = "#sysPublicParam.publicKey")
    public Boolean updateParam(SysPublicParam sysPublicParam) {
        SysPublicParam param = this.getById(sysPublicParam.getPublicId());
        // 系统内置
        if (DictTypeEnum.SYSTEM.getType().equals(param.getSystem())) {
            throw new CheckedException("系统内置参数不能删除");
        }
        //键重复判断
        Long exist = this.baseMapper.selectCount(Wrappers.<SysPublicParam>query().lambda()
                .eq(SysPublicParam::getPublicKey, sysPublicParam.getPublicKey()));
        if(exist>1){
            throw new CheckedException("参数键不能重复");
        }
        return this.updateById(sysPublicParam);
    }

    @Override
    @CacheEvict(value = CacheConstants.PARAMS_DETAILS, allEntries = true)
    public Boolean removeParam(Long publicId) {
        SysPublicParam param = this.getById(publicId);
        // 系统内置
        if (DictTypeEnum.SYSTEM.getType().equals(param.getSystem())) {
            throw new CheckedException("系统内置参数不能删除");
        }
        return this.removeById(publicId);
    }

}
