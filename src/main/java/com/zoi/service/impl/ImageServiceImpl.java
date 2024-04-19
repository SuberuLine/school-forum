package com.zoi.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zoi.entity.dto.Account;
import com.zoi.mapper.AccountMapper;
import com.zoi.service.ImageService;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Resource
    MinioClient minioClient;

    @Resource
    AccountMapper accountMapper;

    @Override
    public String uploadAvatar(MultipartFile file, int id) throws IOException {
        String imageName = UUID.randomUUID().toString().replace("-", "");
        imageName = "/avatar/" + imageName;
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket("forum")
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(imageName)
                .build();
        try {
            minioClient.putObject(putObjectArgs);
            if (accountMapper.update(null, Wrappers.<Account>update()
                    .eq("id", id).set("avatar", imageName)) > 0) {
                return imageName;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("图片上传出错："+ e.getMessage());
            return null;
        }
    }

    @Override
    public void fetchImageFromMinio(OutputStream stream, String image) throws Exception {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket("forum")
                .object(image)
                .build();
        GetObjectResponse response = minioClient.getObject(args);
        IOUtils.copy(response, stream);
    }
}
