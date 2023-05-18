package com.flipped.redissentineldemo.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;

@RestController
public class RedisController {

    private static final String KEY = "REDIS_SENTINEL";

    @Resource
    private StringRedisTemplate template;

    @GetMapping("/{value}")
    public void addToSet(@PathVariable String value) {
        this.template.opsForSet().add(KEY, value);
    }

    @GetMapping("/get")
    public Set<String> getKeyValues() {
        return this.template.opsForSet().members(KEY);
    }

}