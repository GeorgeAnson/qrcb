package com.qrcb.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qrcb.admin.api.entity.SysFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author Anson
 * @Create 2023-11-07
 * @Description 文件管理 Service 接口 <br/>
 */

public interface SysFileService extends IService<SysFile> {

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return {@link Map}
     */
    Map<String, String> uploadFile(MultipartFile file);

    /**
     * 读取文件
     *
     * @param bucket   桶名称
     * @param fileName 文件名称
     * @param response 输出流
     */
    void getFile(String bucket, String fileName, HttpServletResponse response);

    /**
     * 删除文件
     *
     * @param id 主键ID
     * @return {@link Boolean}
     */
    Boolean deleteFile(Long id);
}
