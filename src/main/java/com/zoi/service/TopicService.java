package com.zoi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zoi.entity.dto.Interact;
import com.zoi.entity.dto.Topic;
import com.zoi.entity.dto.TopicType;
import com.zoi.entity.vo.request.TopicCreateVO;
import com.zoi.entity.vo.response.TopicDetailVO;
import com.zoi.entity.vo.response.TopicPreviewVO;
import com.zoi.entity.vo.response.TopicTopVO;

import java.util.List;

public interface TopicService extends IService<Topic> {
    List<TopicType> listTypes();
    String createTopic(int uid, TopicCreateVO vo);
    List<TopicPreviewVO> listTopicByPage(int page, int type);
    List<TopicTopVO> listTopTopics();
    TopicDetailVO getTopic(int id);
    void interact(Interact interact, boolean state);
}
