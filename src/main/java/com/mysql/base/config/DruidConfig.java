package com.mysql.base.config;

import com.alibaba.druid.pool.DruidDataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DruidConfig {

    /**
     * 踩坑：
     * 注意：下面这个配置类不要删除，否则可能导致Druid无法正确加载
     *
     * Druid Starter 的自动配置类 DruidDataSourceAutoConfigure
     * 在某些条件下可能没有被触发（例如类路径冲突、Spring Boot 版本兼容性问题等）。
     * 手动创建 Bean 是最直接、最可靠的方式，并且仍然可以利用 @ConfigurationProperties 绑定配置文件中的属性
     *
     * @return
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSource dataSource() {
        return new DruidDataSource();
    }
}