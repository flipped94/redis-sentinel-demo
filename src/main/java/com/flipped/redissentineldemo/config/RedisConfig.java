package com.flipped.redissentineldemo.config;

import io.lettuce.core.ReadFrom;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import javax.annotation.Resource;

@Configuration
public class RedisConfig {

    @Resource
    private RedisProperties redisProperties;

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        final RedisSentinelConfiguration sentinelConfiguration = new RedisSentinelConfiguration()
                .master(redisProperties.getSentinel().getMaster());
        final LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.ANY_REPLICA)
                .build();

        redisProperties.getSentinel().getNodes().forEach(node -> {
            final String[] sentinelNodes = node.split(":");
            final String host = sentinelNodes[0];
            final int port = Integer.parseInt(sentinelNodes[1]);
            sentinelConfiguration.sentinel(host, port);
        });

        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(sentinelConfiguration, lettuceClientConfiguration);
        connectionFactory.setDatabase(redisProperties.getDatabase());
        return connectionFactory;
    }
}
