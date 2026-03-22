package com.gs.swiftcv.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot 启动类
 */
@SpringBootApplication
@ComponentScan("com.gs.swiftcv")
@MapperScan("com.gs.swiftcv.repository.mapper")
public class Application {

    /**
     * 应用启动入口。
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
