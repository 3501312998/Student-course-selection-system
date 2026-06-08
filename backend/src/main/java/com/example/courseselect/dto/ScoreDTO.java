package com.example.courseselect.dto;

/**
 * 成绩传输对象
 * 封装录入成绩时的学生 ID、课程 ID 和分数
 */

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScoreDTO { // 成绩传输对象
        private Long studentId;    // 学生 ID
        private Long courseId;     // 课程 ID
        private BigDecimal score;  // 成绩分数
}
