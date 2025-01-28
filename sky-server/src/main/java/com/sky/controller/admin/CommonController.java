package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @author : wangshanjie
 * @date : 12:49 2025/1/28
 */

@RestController
@RequestMapping("/admin/common")
@Slf4j
@Api(tags = "通用接口")
public class CommonController {
    @Autowired
    private AliOssUtil aliyunOssUtil;

    @PostMapping("/upload")
    @ApiOperation(value = "上传文件")
    public Result<String> upload(MultipartFile file) {
        log.info("upload file: {}", file.getOriginalFilename());
        //  上传文件到OSS
        // 第一个参数是文件字节数组，第二个参数是文件名
        // 第二个防止文件有重名，使用UUID生成一个新的文件名
        try {
            // 截取文件名的后缀
            String extenstion = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String newFileName = UUID.randomUUID() + extenstion;
            // 上传文件到OSS
            String fileUrl = aliyunOssUtil.upload(file.getBytes(), newFileName);
            return Result.success(fileUrl);
        } catch (Exception e) {
            log.error("upload file error", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
