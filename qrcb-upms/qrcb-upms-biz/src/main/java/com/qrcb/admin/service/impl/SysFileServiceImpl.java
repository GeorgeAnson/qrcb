package com.qrcb.admin.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.amazonaws.services.s3.model.S3Object;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qrcb.admin.api.entity.SysFile;
import com.qrcb.admin.mapper.SysFileMapper;
import com.qrcb.admin.service.SysFileService;
import com.qrcb.common.core.security.util.SecurityUtils;
import com.qrcb.common.extension.oss.OssProperties;
import com.qrcb.common.extension.oss.service.OssTemplate;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 文件管理 Service 实现类 <br/>
 */

@Slf4j
@Service
@AllArgsConstructor
public class SysFileServiceImpl extends ServiceImpl<SysFileMapper, SysFile> implements SysFileService {

    private final OssProperties ossProperties;

    private final OssTemplate minioTemplate;

    @Override
    @SneakyThrows
    public Map<String, String> uploadFile(MultipartFile file) {
        String fileName = IdUtil.simpleUUID() + StrUtil.DOT + FileUtil.extName(file.getOriginalFilename());
        Map<String, String> resultMap = new HashMap<>(4);
        resultMap.put("bucketName", ossProperties.getBucketName());
        resultMap.put("fileName", fileName);
        resultMap.put("url", String.format("/admin/sys-file/%s/%s", ossProperties.getBucketName(), fileName));

        minioTemplate.putObject(ossProperties.getBucketName(), fileName, file.getInputStream());
        // 文件管理数据记录,收集管理追踪文件
        fileLog(file, fileName);
        return resultMap;
    }

    @Override
    public void getFile(String bucket, String fileName, HttpServletResponse response) {
        try (S3Object s3Object = minioTemplate.getObject(bucket, fileName)) {
            response.setContentType("application/octet-stream; charset=UTF-8");
            IoUtil.copy(s3Object.getObjectContent(), response.getOutputStream());
        } catch (Exception e) {
            log.error("文件读取异常: {}", e.getLocalizedMessage());
        }
    }

    @Override
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteFile(Long id) {
        SysFile file = this.getById(id);
        minioTemplate.removeObject(ossProperties.getBucketName(), file.getFileName());
        return this.removeById(id);
    }

    /**
     * 文件管理数据记录,收集管理追踪文件
     *
     * @param file     上传文件格式
     * @param fileName 文件名
     */
    private void fileLog(MultipartFile file, String fileName) {
        SysFile sysFile = new SysFile();
        sysFile.setFileName(fileName);
        sysFile.setOriginal(file.getOriginalFilename());
        sysFile.setFileSize(file.getSize());
        sysFile.setType(FileUtil.extName(file.getOriginalFilename()));
        sysFile.setBucketName(ossProperties.getBucketName());
        sysFile.setCreateUser(SecurityUtils.getUser().getUsername());
        this.save(sysFile);
    }
}
