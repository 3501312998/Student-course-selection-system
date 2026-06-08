package com.example.courseselect;

/**
 * 学生选课系统 - 应用入口
 * Spring Boot 启动类，负责初始化整个后端应用
 */

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CourseSelectApplication {
    public static void main(String[] args) {
        SpringApplication.run(CourseSelectApplication.class, args);
    }
}
