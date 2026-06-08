package com.example.courseselect.mapper;

/**
 * 课程数据访问层
 * 定义 Course 实体的数据库操作接口，提供按教师/学期查询
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.courseselect.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> { // 课程数据访问

    @Select("SELECT COUNT(*) FROM t_course WHERE deleted = 0")
        long countAll(); // 统计课程总数

    @Select("SELECT COUNT(*) FROM t_course WHERE teacher_id = #{teacherId} AND deleted = 0")
        long countByTeacherId(@Param("teacherId") Long teacherId); // 统计某教师授课数

    @Select("SELECT * FROM t_course WHERE teacher_id = #{teacherId} AND deleted = 0")
        List<Course> findByTeacherId(@Param("teacherId") Long teacherId); // 按教师查询课程

    @Select("SELECT * FROM t_course WHERE semester = #{semester} AND deleted = 0")
        List<Course> findBySemester(@Param("semester") String semester); // 按学期查询课程
}
