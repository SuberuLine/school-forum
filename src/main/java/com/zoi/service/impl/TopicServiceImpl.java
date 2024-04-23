package com.zoi.service.impl;

import com.zoi.entity.dto.TopicType;
import com.zoi.mapper.TopicTypeMapper;
import com.zoi.service.TopicService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    @Resource
    TopicTypeMapper topicTypeMapper;

    @Override
    public List<TopicType> listTypes() {
        return topicTypeMapper.selectList(null);
    }
}
