package com.zoi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zoi.entity.dto.Topic;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TopicMapper extends BaseMapper<Topic> {
}
