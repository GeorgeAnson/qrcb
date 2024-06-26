package com.qrcb.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qrcb.admin.api.entity.SysFile;
import com.qrcb.admin.service.SysFileService;
import com.qrcb.common.core.assemble.util.R;
import com.qrcb.common.core.log.annotation.SysLog;
import com.qrcb.common.core.security.annotation.Inner;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author Anson
 * @Create 2023-11-08
 * @Description 文件管理 <br/>
 */

@RestController
@AllArgsConstructor
@RequestMapping("/sys-file")
@Api(value = "sys-file", tags = "文件管理")
public class SysFileController {

    private final SysFileService sysFileService;

    /**
     * 分页查询
     *
     * @param page    分页对象
     * @param sysFile 文件管理
     * @return {@link SysFile} Page
     */
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @GetMapping("/page")
    public R<IPage<SysFile>> getSysFilePage(Page<SysFile> page, SysFile sysFile) {
        return R.ok(sysFileService.page(page, Wrappers.query(sysFile)));
    }

    /**
     * 通过id删除文件管理
     *
     * @param id id
     * @return {@link Boolean}
     */
    @ApiOperation(value = "通过id删除文件管理", notes = "通过id删除文件管理")
    @SysLog("删除文件管理")
    @DeleteMapping("/{id}")
    @PreAuthorize("@pms.hasPermission('sys_file_del')")
    public R<Boolean> removeById(@PathVariable Long id) {
        return R.ok(sysFileService.deleteFile(id));
    }

    /**
     * 上传文件 文件名采用uuid,避免原始文件名中带"-"符号导致下载的时候解析出现异常
     *
     * @param file 资源
     * @return {@link Map} R(/ admin / bucketName / filename)
     */
    @PostMapping(value = "/upload")
    public R<Map<String, String>> upload(@RequestPart("file") MultipartFile file) {
        return R.ok(sysFileService.uploadFile(file));
    }

    /**
     * 获取文件
     *
     * @param bucket   桶名称
     * @param fileName 文件空间/名称
     * @param response {@link HttpServletResponse}
     */
    @Inner(false)
    @GetMapping("/{bucket}/{fileName}")
    public void file(@PathVariable String bucket, @PathVariable String fileName, HttpServletResponse response) {
        sysFileService.getFile(bucket, fileName, response);
    }

}
