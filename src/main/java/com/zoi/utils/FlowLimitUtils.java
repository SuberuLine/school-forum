package com.zoi.utils;

import com.zoi.filter.FlowLimitFilter;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Redis限流工具类
 */
@Component
public class FlowLimitUtils {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    private static final LimitAction defaultAction = overclock -> !overclock;

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

    /**
     * 针对于单次频率限制，请求成功后，在冷却时间内不得再次进行请求
     * 如3秒内不能再次发起请求，如果不听劝阻继续发起请求，将限制更长时间
     * @param key 键
     * @param frequency 请求频率
     * @param baseTime 基础限制时间
     * @param upgradeTime 升级限制时间
     * @return 是否通过限流检查
     */
    public boolean limitOnceUpgradeCheck(String key, int frequency, int baseTime, int upgradeTime){
        return this.internalCheck(key, frequency, baseTime, (overclock) -> {
            if (overclock)
                stringRedisTemplate.opsForValue().set(key, "1", upgradeTime, TimeUnit.SECONDS);
            return false;
        });
    }

    /**
     * 针对于在时间段内多次请求限制，如3秒内限制请求20次，超出频率则封禁一段时间
     * @param counterKey 计数键
     * @param blockKey 封禁键
     * @param blockTime 封禁时间
     * @param frequency 请求频率
     * @param period 计数周期
     * @return 是否通过限流检查
     */
    public boolean limitPeriodCheck(String counterKey, String blockKey, int blockTime, int frequency, int period){
        return this.internalCheck(counterKey, frequency, period, (overclock) -> {
            if (overclock)
                stringRedisTemplate.opsForValue().set(blockKey, "", blockTime, TimeUnit.SECONDS);
            return !overclock;
        });
    }

    /**
     * 内部使用请求限制主要逻辑
     * @param key 计数键
     * @param frequency 请求频率
     * @param period 计数周期
     * @param action 限制行为与策略
     * @return 是否通过限流检查
     */
    private boolean internalCheck(String key, int frequency, int period, LimitAction action){
        String count = stringRedisTemplate.opsForValue().get(key);
        if (count != null) {
            long value = Optional.ofNullable(stringRedisTemplate.opsForValue().increment(key)).orElse(0L);
            int c = Integer.parseInt(count);
            if(value != c + 1)
                stringRedisTemplate.expire(key, period, TimeUnit.SECONDS);
            return action.run(value > frequency);
        } else {
            stringRedisTemplate.opsForValue().set(key, "1", period, TimeUnit.SECONDS);
            return true;
        }
    }

    /**
     * 针对在于时间段内的多次请求进行限制
     * @param counterKey 计数键
     * @param frequency 请求频率
     * @param period 计数周期
     * @return 是否通过限流检查
     */
    public boolean limitPeriodCounterCheck(String counterKey, int frequency, int period) {
        return this.internalCheck(counterKey, frequency, period, defaultAction);
    }

    /**
     * 内部使用，限制行为与策略
     */
    private interface LimitAction {
        boolean run(boolean overclock);
    }
}
