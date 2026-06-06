package com.example.courseselect.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseDTO {
    @NotBlank(message = "课程名称不能为空")
    private String courseName;

    @NotBlank(message = "课程编号不能为空")
    private String courseCode;

    private Long teacherId;

    @NotNull(message = "学分不能为空")
    private Integer credit;

    @NotNull(message = "容量不能为空")
    private Integer maxCapacity;

    private String semester;

    private String schedule;

    private String classroom;

    private String description;
}
