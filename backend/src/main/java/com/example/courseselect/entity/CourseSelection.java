package com.example.courseselect.entity;

/**
 * 选课记录实体类
 * 对应数据库 t_course_selection 表，记录学生选课信息及成绩
 */

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_course_selection") // 选课记录表
public class CourseSelection {
        @TableId(type = IdType.AUTO) // 自增主键
        private Long id;                   // 记录 ID

        private Long studentId;            // 学生 ID

        private Long courseId;             // 课程 ID

        private Integer status;            // 状态: 1在学习 2已退课 3已结课

        private BigDecimal score;          // 成绩

        private LocalDateTime selectTime;  // 选课时间

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
