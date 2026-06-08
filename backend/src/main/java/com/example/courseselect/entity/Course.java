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
@TableName("t_course") // 课程表
public class Course {
        @TableId(type = IdType.AUTO) // 自增主键
        private Long id;                   // 课程 ID

        private String courseName;          // 课程名称

        private String courseCode;          // 课程编号（唯一）

        private Long teacherId;             // 授课教师 ID

        private Integer credit;             // 学分

        private Integer maxCapacity;        // 最大容量

        private Integer currentCount;       // 已选人数

        private String semester;            // 学期

        private String schedule;            // 上课时间

        private String classroom;           // 上课地点

        private String description;         // 课程描述

        private Integer status;             // 状态: 1开放 0关闭

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
