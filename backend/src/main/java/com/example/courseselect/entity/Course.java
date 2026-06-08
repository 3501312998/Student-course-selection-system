package com.example.courseselect.entity;

/**
 * 课程实体类
 * 对应数据库 t_course 表，存储课程基本信息
 */

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_course")
public class Course {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String courseName;

    private String courseCode;

    private Long teacherId;

    private Integer credit;

    private Integer maxCapacity;

    private Integer currentCount;

    private String semester;

    private String schedule;

    private String classroom;

    private String description;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
