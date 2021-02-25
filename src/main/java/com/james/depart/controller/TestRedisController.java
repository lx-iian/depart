package com.james.depart.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

/**
 * @author james
 * @date 2021-02-25
 */
@RestController("redisTest")
@Slf4j
public class TestRedisController {

    final RedisTemplate redisTemplate;

    final StringRedisTemplate stringRedisTemplate;

    public TestRedisController(RedisTemplate redisTemplate, StringRedisTemplate stringRedisTemplate) {
        this.redisTemplate = redisTemplate;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @GetMapping("set/{key}/{value}")
    public void set(@PathVariable String key, @PathVariable String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @GetMapping("get/{key}")
    public String show(@PathVariable String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @GetMapping("set2/{key}/{value}")
    public void set2(@PathVariable String key, @PathVariable String value) {
        // 可选，自定义序列化方法
        stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
                byte[] serializeKey = redisSerializer.serialize(key);
                byte[] serializeValue = redisSerializer.serialize(value);
                return redisConnection.setNX(serializeKey, serializeValue);
            }
        });
    }
}
