package com.zoi.controller;

import com.zoi.entity.RestBean;
import com.zoi.entity.vo.request.TopicCreateVO;
import com.zoi.entity.vo.response.TopicTypeVO;
import com.zoi.entity.vo.response.WeatherVO;
import com.zoi.service.TopicService;
import com.zoi.service.WeatherService;
import com.zoi.utils.Const;
import com.zoi.utils.ControllerUtils;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    @Resource
    WeatherService weatherService;

    @Resource
    TopicService topicService;

    @Resource
    ControllerUtils controllerUtils;

    @GetMapping("/weather")
    public RestBean<WeatherVO> weather(double longitude, double latitude) {
        WeatherVO vo = weatherService.fetchWeather(longitude, latitude);
        return vo == null ?
                RestBean.failure(400, "获取地理位置信息失败，请联系管理员！") : RestBean.success(vo);
    }

    @GetMapping("/types")
    public RestBean<List<TopicTypeVO>> listTypes() {
        return RestBean.success(topicService
                .listTypes()
                .stream()
                .map(type -> type.asViewObject(TopicTypeVO.class))
                .toList());
    }

    @PostMapping("/create-topic")
    public RestBean<Void> createTopic(@Valid @RequestBody TopicCreateVO vo,
                                      @RequestAttribute(Const.ATTR_USER_ID) int id) {
        return controllerUtils.messageHandle(() -> topicService.createTopic(id, vo));
    }

}
