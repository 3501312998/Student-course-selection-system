package com.example.courseselect.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.courseselect.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseMapper extends BaseMapper<Course> {

    @Select("SELECT * FROM t_course WHERE teacher_id = #{teacherId} AND deleted = 0")
    List<Course> findByTeacherId(@Param("teacherId") Long teacherId);

    @Select("SELECT * FROM t_course WHERE semester = #{semester} AND deleted = 0")
    List<Course> findBySemester(@Param("semester") String semester);
}
