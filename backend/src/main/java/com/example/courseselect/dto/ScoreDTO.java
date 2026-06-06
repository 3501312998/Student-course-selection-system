package com.example.courseselect.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ScoreDTO {
    private Long studentId;
    private Long courseId;
    private BigDecimal score;
}
