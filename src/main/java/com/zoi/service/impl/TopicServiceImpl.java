package com.zoi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zoi.entity.dto.Topic;
import com.zoi.entity.dto.TopicType;
import com.zoi.entity.vo.request.TopicCreateVO;
import com.zoi.entity.vo.response.TopicPreviewVO;
import com.zoi.entity.vo.response.TopicTopVO;
import com.zoi.mapper.TopicMapper;
import com.zoi.mapper.TopicTypeMapper;
import com.zoi.service.TopicService;
import com.zoi.utils.CacheUtils;
import com.zoi.utils.Const;
import com.zoi.utils.FlowLimitUtils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Resource
    TopicTypeMapper topicTypeMapper;

    @Resource
    FlowLimitUtils flowLimitUtils;

    @Resource
    CacheUtils cacheUtils;

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
            cacheUtils.deleteCacheWithPattern(Const.FORUM_TOPIC_PREVIEW_CACHE + "*");
            return null;
        } else {
            return "内部错误，请联系管理员";
        }
    }

    @Override
    public List<TopicPreviewVO> listTopicByPage(int page, int type) {
        String key = Const.FORUM_TOPIC_PREVIEW_CACHE + page + ":" + type;
        List<TopicPreviewVO> list = cacheUtils.takeListFromCache(key, TopicPreviewVO.class);
        if (list != null) return list;
        List<Topic> topics;
        if (type == 0) {
            topics = baseMapper.topicList(page * 10);
        } else {
            topics = baseMapper.topicListByType(page * 10, type);
        }
        if (topics.isEmpty()) return null;
        list = topics.stream().map(this::resolveToPreview).toList();
        cacheUtils.saveListToCache(key, list, 60);
        return list;
    }

    @Override
    public List<TopicTopVO> listTopTopics() {
        List<Topic> topics = baseMapper.selectList(Wrappers.<Topic>query()
                .select("id", "title", "time")
                .eq("top", 1));
        return topics.stream().map(topic -> {
            TopicTopVO vo = new TopicTopVO();
            BeanUtils.copyProperties(topic, vo);
            return vo;
        }).toList();
    }

    private TopicPreviewVO resolveToPreview(Topic topic) {
        TopicPreviewVO vo = new TopicPreviewVO();
        BeanUtils.copyProperties(topic, vo);
        List<String> images = new ArrayList<>();
        StringBuilder previewText = new StringBuilder();
        JSONArray ops = JSONObject.parseObject(topic.getContent()).getJSONArray("ops");
        for (Object op : ops) {
            Object insert = JSONObject.from(op).get("insert");
            if (insert instanceof String text) {
                if(previewText.length() >= 300) continue;
                previewText.append(text);
            } else if (insert instanceof Map<?, ?> map) {
                Optional.ofNullable(map.get("image"))
                        .ifPresent(obj -> images.add(obj.toString()));
            }
        }
        vo.setText(previewText.length() > 300 ? previewText.substring(0, 300) : previewText.toString());
        vo.setImages(images);
        return vo;
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
