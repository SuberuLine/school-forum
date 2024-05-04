package com.zoi.utils;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class CacheUtils {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    public <T> T takeFromCache(String key, Class<T> dataType) {
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s == null) return null;
        return JSONObject.parseObject(s).to(dataType);
    }

    public <T> List<T> takeListFromCache(String key, Class<T> itemType){
        String s = stringRedisTemplate.opsForValue().get(key);
        if (s == null) return null;
        return JSONArray.parseArray(s).toList(itemType);
    }
    public <T> void saveListToCache(String key, List<T> list, long expire) {
        stringRedisTemplate.opsForValue()
                .set(key, JSONArray.from(list).toJSONString(), expire, TimeUnit.SECONDS);
    }

    public <T> void saveToCache(String key, T data, long expire) {
        stringRedisTemplate.opsForValue()
                .set(key, JSONArray.from(data).toJSONString(), expire, TimeUnit.SECONDS);
    }

    public void deleteCache(String key) {
        stringRedisTemplate.delete(key);
    }

    public void deleteCacheWithPattern(String pattern) {
        Set<String> keys = stringRedisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            stringRedisTemplate.delete(keys);
        }
    }

}
