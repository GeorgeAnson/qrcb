package com.qrcb.admin.service.impl;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qrcb.admin.api.dto.SysOauthClientDetailsDto;
import com.qrcb.admin.api.entity.SysOauthClientDetails;
import com.qrcb.admin.config.ClientDetailsInitRunner;
import com.qrcb.admin.mapper.SysOauthClientDetailsMapper;
import com.qrcb.admin.service.SysOauthClientDetailsService;
import com.qrcb.common.core.assemble.constant.CacheConstants;
import com.qrcb.common.core.assemble.constant.CommonConstants;
import com.qrcb.common.core.assemble.util.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description SysOauthClientDetails Service 实现类<br/>
 */

@Service
@RequiredArgsConstructor
public class SysOauthClientDetailsServiceImpl extends ServiceImpl<SysOauthClientDetailsMapper, SysOauthClientDetails>
        implements SysOauthClientDetailsService {

    @Override
    @CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientId")
    public Boolean removeByClientId(String clientId) {
        // 更新库
        baseMapper.delete(Wrappers.<SysOauthClientDetails>lambdaQuery()
                .eq(SysOauthClientDetails::getClientId, clientId));
        // 更新Redis
        SpringContextHolder.publishEvent(new ClientDetailsInitRunner.ClientDetailsInitEvent(clientId));
        return Boolean.TRUE;
    }

    @Override
    @CacheEvict(value = CacheConstants.CLIENT_DETAILS_KEY, key = "#clientDetailsDTO.clientId")
    public Boolean updateClientById(SysOauthClientDetailsDto clientDetailsDto) {
        // copy dto 对象
        SysOauthClientDetails clientDetails = new SysOauthClientDetails();
        BeanUtils.copyProperties(clientDetailsDto, clientDetails);

        // 获取扩展信息,插入开关相关
        String information = clientDetailsDto.getAdditionalInformation();
        JSONObject informationObj = JSONUtil.parseObj(information)
                .set(CommonConstants.CAPTCHA_FLAG, clientDetailsDto.getCaptchaFlag())
                .set(CommonConstants.ENC_FLAG, clientDetailsDto.getEncFlag());
        clientDetails.setAdditionalInformation(informationObj.toString());

        // 更新数据库
        baseMapper.updateById(clientDetails);
        // 更新Redis
        SpringContextHolder.publishEvent(new ClientDetailsInitRunner.ClientDetailsInitEvent(clientDetails));
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveClient(SysOauthClientDetailsDto clientDetailsDto) {
        // copy dto 对象
        SysOauthClientDetails clientDetails = new SysOauthClientDetails();
        BeanUtils.copyProperties(clientDetailsDto, clientDetails);

        // 获取扩展信息,插入开关相关
        String information = clientDetailsDto.getAdditionalInformation();
        JSONUtil.parseObj(information).set(CommonConstants.CAPTCHA_FLAG, clientDetailsDto.getCaptchaFlag())
                .set(CommonConstants.ENC_FLAG, clientDetailsDto.getEncFlag());

        // 插入数据
        this.baseMapper.insert(clientDetails);
        // 更新Redis
        SpringContextHolder.publishEvent(new ClientDetailsInitRunner.ClientDetailsInitEvent(clientDetails));
        return Boolean.TRUE;
    }

    @Override
    public Page<SysOauthClientDetailsDto> queryPage(Page<SysOauthClientDetails> page, SysOauthClientDetails query) {
        Page<SysOauthClientDetails> selectPage = baseMapper.selectPage(page, Wrappers.query(query));

        // 处理扩展字段组装dto
        List<SysOauthClientDetailsDto> collect = selectPage.getRecords().stream().map(details -> {
            String information = details.getAdditionalInformation();
            String captchaFlag = JSONUtil.parseObj(information).getStr(CommonConstants.CAPTCHA_FLAG);
            String encFlag = JSONUtil.parseObj(information).getStr(CommonConstants.ENC_FLAG);
            SysOauthClientDetailsDto dto = new SysOauthClientDetailsDto();
            BeanUtils.copyProperties(details, dto);
            dto.setCaptchaFlag(captchaFlag);
            dto.setEncFlag(encFlag);
            return dto;
        }).collect(Collectors.toList());

        // 构建dto page 对象
        Page<SysOauthClientDetailsDto> dtoPage = new Page<>(page.getCurrent(), page.getSize(), selectPage.getTotal());
        dtoPage.setRecords(collect);
        return dtoPage;
    }
}
