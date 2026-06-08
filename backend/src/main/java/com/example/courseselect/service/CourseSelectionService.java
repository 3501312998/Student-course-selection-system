package com.example.courseselect.service;

/**
 * 选课服务接口
 * 定义选课、退课、课表查询、成绩管理等业务方法
 */

import com.example.courseselect.entity.CourseSelection;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CourseSelectionService { // 选课服务接口
        void selectCourse(Long studentId, Long courseId); // 选课
        void dropCourse(Long studentId, Long courseId); // 退课
        List<Map<String, Object>> getStudentSchedule(Long studentId); // 查询学生课表
        List<Map<String, Object>> getStudentGrades(Long studentId); // 查询学生成绩
        void setScore(Long teacherId, Long studentId, Long courseId, BigDecimal score); // 录入/修改成绩
        List<Map<String, Object>> getCourseStudents(Long courseId); // 查询某课程的学生名单
}
