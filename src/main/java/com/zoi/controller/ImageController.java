package com.zoi.controller;

import com.zoi.entity.RestBean;
import com.zoi.service.ImageService;
import com.zoi.utils.Const;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/api/image")
public class ImageController {

    @Resource
    ImageService imageService;

    @PostMapping("/avatar")
    public RestBean<String> uploadAvatar(@RequestParam("file") MultipartFile file,
                                       @RequestAttribute(Const.ATTR_USER_ID) int id) throws IOException {
        if (file.getSize() > 1024 * 1024)
            return RestBean.failure(400, "头像大小不能超过1mb");
        log.info("正在进行头像上传操作...");
        String url = imageService.uploadAvatar(file, id);
        if (url != null) {
            log.info("头像上传成功，大小：" + file.getSize());
            return RestBean.success(url);
        } else {
            return RestBean.failure(400, "头像上传失败，请联系管理员");
        }
    }

}
