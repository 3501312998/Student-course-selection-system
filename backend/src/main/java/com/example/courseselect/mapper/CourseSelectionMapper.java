package com.example.courseselect.mapper;

/**
 * 选课记录数据访问层
 * 定义 CourseSelection 实体的数据库操作接口
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.courseselect.entity.CourseSelection;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CourseSelectionMapper extends BaseMapper<CourseSelection> {

    @Select("SELECT COUNT(*) FROM t_course_selection WHERE deleted = 0")
    long countAll();

    @Select("SELECT COUNT(*) FROM t_course_selection WHERE student_id = #{studentId} AND status = 1 AND deleted = 0")
    long countSelectedByStudentId(@Param("studentId") Long studentId);

    @Select("SELECT COUNT(*) FROM t_course_selection WHERE course_id = #{courseId} AND status = 1 AND deleted = 0")
    long countByCourseId(@Param("courseId") Long courseId);

    @Select("SELECT IFNULL(SUM(c.credit), 0) FROM t_course_selection cs JOIN t_course c ON cs.course_id = c.id WHERE cs.student_id = #{studentId} AND cs.status = 1 AND cs.deleted = 0 AND c.deleted = 0")
    long sumCreditsByStudentId(@Param("studentId") Long studentId);

    @Select("SELECT COUNT(*) FROM t_course_selection WHERE course_id IN (SELECT id FROM t_course WHERE teacher_id = #{teacherId} AND deleted = 0) AND status = 1 AND deleted = 0")
    long countStudentsByTeacherId(@Param("teacherId") Long teacherId);

    @Select("SELECT COUNT(*) FROM t_course_selection cs JOIN t_course c ON cs.course_id = c.id WHERE c.teacher_id = #{teacherId} AND cs.score IS NULL AND cs.status = 1 AND cs.deleted = 0 AND c.deleted = 0")
    long countUngradedByTeacherId(@Param("teacherId") Long teacherId);

    @Select("SELECT * FROM t_course_selection WHERE student_id = #{studentId} AND status = 1 AND deleted = 0")
    List<CourseSelection> findSelectedByStudentId(@Param("studentId") Long studentId);

    @Select("SELECT * FROM t_course_selection WHERE course_id = #{courseId} AND status IN (1, 3) AND deleted = 0")
    List<CourseSelection> findByCourseId(@Param("courseId") Long courseId);

    @Select("SELECT * FROM t_course_selection WHERE student_id = #{studentId} AND course_id = #{courseId} AND deleted = 0")
    CourseSelection findByStudentAndCourse(@Param("studentId") Long studentId, @Param("courseId") Long courseId);

    @Select("SELECT * FROM t_course_selection WHERE student_id = #{studentId} AND deleted = 0")
    List<CourseSelection> findAllByStudentId(@Param("studentId") Long studentId);
}
