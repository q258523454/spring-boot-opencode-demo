package com.mysql.base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.mysql.base.repository"})
public class OpencodeTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpencodeTestApplication.class, args);
    }

}