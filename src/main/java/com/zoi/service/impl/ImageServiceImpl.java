package com.zoi.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoi.entity.dto.Account;
import com.zoi.entity.dto.StoreImage;
import com.zoi.mapper.AccountMapper;
import com.zoi.mapper.ImageStoreMapper;
import com.zoi.service.ImageService;
import com.zoi.utils.Const;
import com.zoi.utils.FlowLimitUtils;
import io.minio.*;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class ImageServiceImpl extends ServiceImpl<ImageStoreMapper, StoreImage> implements ImageService {

    @Resource
    MinioClient minioClient;

    @Resource
    AccountMapper accountMapper;

    @Resource
    FlowLimitUtils flowLimitUtils;

    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

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
            String avatar = accountMapper.selectById(id).getAvatar();
            this.deleteOldAvatar(avatar);
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
    public String uploadImage(MultipartFile file, int id) throws IOException {
        String key = Const.FORUM_IMAGE_COUNTER + id;
        if (!flowLimitUtils.limitPeriodCounterCheck(key, 20, 3600))
            return null;
        String imageName = UUID.randomUUID().toString().replace("-", "");
        Date date = new Date();
        imageName = "/cache/" + format.format(date) + "/" + imageName;
        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .bucket("forum")
                .stream(file.getInputStream(), file.getSize(), -1)
                .object(imageName)
                .build();
        try {
            minioClient.putObject(putObjectArgs);
            if (this.save(new StoreImage(id, imageName ,date))) {
                return imageName;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("图片上传出现问题：" + e.getMessage(), e);
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

    /**
     * 删除用户旧头像
     * @param avatar 旧头像地址
     * @throws Exception
     */
    private void deleteOldAvatar(String avatar) throws Exception {
        if (avatar == null || avatar.isEmpty()) return;
        RemoveObjectArgs remove = RemoveObjectArgs.builder()
                .bucket("forum")
                .object(avatar)
                .build();
        minioClient.removeObject(remove);
    }
}
