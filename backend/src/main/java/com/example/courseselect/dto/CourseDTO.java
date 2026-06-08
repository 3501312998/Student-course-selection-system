package com.example.courseselect.dto;

/**
 * 课程传输对象
 * 封装创建/更新课程时提交的课程信息，含参数校验规则
 */

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
