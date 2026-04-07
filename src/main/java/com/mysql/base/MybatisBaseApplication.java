package com.mysql.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.mysql.base.dao"})
public class MybatisBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(MybatisBaseApplication.class, args);
    }

}