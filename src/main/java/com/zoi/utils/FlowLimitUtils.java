package com.zoi.utils;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis限流工具类
 */
@Component
public class FlowLimitUtils {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 判断用户是否在申请验证码冷却期中
     * @param key 用户标识
     * @param blockTime 冷却时间
     * @return bool
     */
    public boolean limitOnceCheck(String key, int blockTime) {
        // 判断是否在冷却状态
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))) {
            return false;
        } else {
            // 存入一条数据代表用户正在冷却状态
            stringRedisTemplate.opsForValue().set(key, "", blockTime, TimeUnit.SECONDS);
        }
        return true;
    }
}
