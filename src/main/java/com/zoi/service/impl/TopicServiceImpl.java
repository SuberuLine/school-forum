package com.zoi.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoi.entity.dto.Topic;
import com.zoi.entity.dto.TopicType;
import com.zoi.entity.vo.request.TopicCreateVO;
import com.zoi.mapper.TopicMapper;
import com.zoi.mapper.TopicTypeMapper;
import com.zoi.service.TopicService;
import com.zoi.utils.Const;
import com.zoi.utils.FlowLimitUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Resource
    TopicTypeMapper topicTypeMapper;

    @Resource
    FlowLimitUtils flowLimitUtils;

    @PostConstruct
    private void initTypes() {
        types = this.listTypes()
                .stream()
                .map(TopicType::getId)
                .collect(Collectors.toSet());
    }

    private Set<Integer> types = null;

    @Override
    public List<TopicType> listTypes() {
        return topicTypeMapper.selectList(null);
    }

    @Override
    public String createTopic(int uid, TopicCreateVO vo) {
        if (!textLimitCheck(vo.getContent()))
            return "文章字数超过限制，请修改后再发布";
        if (!types.contains(vo.getType()))
            return "文章类型非法";
        String key = Const.FORUM_TOPIC_CREATE_COUNTER + uid;
        if (!flowLimitUtils.limitPeriodCounterCheck(key, 3, 3600))
            return "发文频繁，请稍后再试";
        Topic topic = new Topic();
        BeanUtils.copyProperties(vo, topic);
        topic.setContent(vo.getContent().toJSONString());
        topic.setUid(uid);
        topic.setTime(new Date());
        if(this.save(topic)) {
            return null;
        } else {
            return "内部错误，请联系管理员";
        }
    }

    private boolean textLimitCheck(JSONObject object) {
        if (object == null) return false;
        long length = 0;
        for (Object ops : object.getJSONArray("ops")) {
            length += JSONObject.from(ops).getString("insert").length();
            if (length > 20000) return false;
        }
        return true;
    }
}
