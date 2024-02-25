package com.example.demo.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class RedisUtils {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setData(String key, String value, Duration expiredTime) {
        redisTemplate.opsForValue().set(key, value, expiredTime);
    }

    @Transactional(readOnly = true)
    public String getValues(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) {
            return "false";
        }
        return (String) values.get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public boolean checkExistsValue(String value) {
        return !value.equals("false");
    }
}
