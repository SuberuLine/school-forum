package com.zoi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoi.entity.dto.Notification;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}
