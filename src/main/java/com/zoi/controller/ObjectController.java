package com.zoi.controller;

import com.zoi.entity.RestBean;
import com.zoi.service.ImageService;
import io.minio.errors.ErrorResponseException;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ObjectController {

    @Resource
    ImageService imageService;

    @GetMapping("/image/**")
    public void imageFetch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Type", "image/jpg");
        this.fetchImage(request, response);
    }

    private void fetchImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String imagePath = request.getServletPath().substring(7);
        ServletOutputStream outputStream = response.getOutputStream();
        if (imagePath.length() <= 13) {
            response.setStatus(404);
            outputStream.println(RestBean.failure(404, "Not Found").toString());
        } else {
            try {
                imageService.fetchImageFromMinio(outputStream, imagePath);
                response.setHeader("Cache-Control", "max-age=2592000");
            } catch (ErrorResponseException e) {
                if (e.response().code() == 404) {
                    response.setStatus(404);
                    outputStream.println(RestBean.failure(404, "Not Found").toString());
                } else {
                    log.error("从Minio获取图片出现异常:" + e.getMessage(), e);
                }
            }
        }

    }
}
