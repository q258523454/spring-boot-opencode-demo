package com.mysql.base.config;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Redisson配置类
 * 
 * @author zhang
 * @since 2026-04-13
 */
@Configuration
@Slf4j
public class RedissonConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password:}")
    private String password;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        String address = "redis://" + host + ":" + port;
        
        config.useSingleServer()
                .setAddress(address)
                .setPassword(password.isEmpty() ? null : password)
                .setConnectionPoolSize(10)
                .setConnectionMinimumIdleSize(5)
                .setIdleConnectionTimeout(10000)
                .setConnectTimeout(3000)
                .setTimeout(3000);
        
        log.info("Redisson client initialized, connecting to: {}", address);
        return Redisson.create(config);
    }
}
