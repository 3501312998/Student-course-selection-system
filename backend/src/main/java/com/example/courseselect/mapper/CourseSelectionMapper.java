package com.example.courseselect.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.courseselect.entity.CourseSelection;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseSelectionMapper extends BaseMapper<CourseSelection> {

    @Select("SELECT * FROM t_course_selection WHERE student_id = #{studentId} AND status = 1 AND deleted = 0")
    List<CourseSelection> findSelectedByStudentId(@Param("studentId") Long studentId);

    @Select("SELECT * FROM t_course_selection WHERE course_id = #{courseId} AND status = 1 AND deleted = 0")
    List<CourseSelection> findByCourseId(@Param("courseId") Long courseId);

    @Select("SELECT * FROM t_course_selection WHERE student_id = #{studentId} AND course_id = #{courseId} AND deleted = 0")
    CourseSelection findByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Select("SELECT * FROM t_course_selection WHERE student_id = #{studentId} AND deleted = 0")
    List<CourseSelection> findAllByStudentId(@Param("studentId") Long studentId);
}
