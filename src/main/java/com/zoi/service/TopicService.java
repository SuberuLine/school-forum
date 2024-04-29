package com.zoi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoi.entity.dto.Topic;
import com.zoi.entity.dto.TopicType;
import com.zoi.entity.vo.request.TopicCreateVO;

import java.util.List;

public interface TopicService extends IService<Topic> {
    List<TopicType> listTypes();
    String createTopic(int uid, TopicCreateVO vo);
}
