package com.example.courseselect.dto;

/**
 * 成绩传输对象
 * 封装录入成绩时的学生 ID、课程 ID 和分数
 */

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScoreDTO {
    private Long studentId;
    private Long courseId;
    private BigDecimal score;
}
