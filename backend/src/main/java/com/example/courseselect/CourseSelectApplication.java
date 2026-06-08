package com.example.courseselect;

/**
 * 学生选课系统 - 应用入口
 * Spring Boot 启动类，负责初始化整个后端应用
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseSelectApplication { // 应用主类
    public static void main(String[] args) {
                SpringApplication.run(CourseSelectApplication.class, args); // 启动 Spring Boot 容器
    }
}
