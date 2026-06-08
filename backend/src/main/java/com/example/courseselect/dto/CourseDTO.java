package com.example.courseselect.dto;

/**
 * 课程传输对象
 * 封装创建/更新课程时提交的课程信息，含参数校验规则
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CourseDTO { // 课程传输对象
    @NotBlank(message = "课程名称不能为空")
        private String courseName;   // 课程名称

    @NotBlank(message = "课程编号不能为空")
        private String courseCode;   // 课程编号

        private Long teacherId;      // 授课教师 ID

    @NotNull(message = "学分不能为空")
        private Integer credit;      // 学分

    @NotNull(message = "容量不能为空")
        private Integer maxCapacity; // 最大容量

        private String semester;     // 学期

        private String schedule;     // 上课时间

        private String classroom;    // 上课地点

        private String description;  // 课程描述
}
