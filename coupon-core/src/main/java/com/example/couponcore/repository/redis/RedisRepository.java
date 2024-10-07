package com.example.couponcore.repository.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public boolean zAdd(String key, String value, double score){
        return redisTemplate.opsForZSet().add(key, value, score);
    }
}
