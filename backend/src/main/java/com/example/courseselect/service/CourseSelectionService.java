package com.example.courseselect.service;

import com.example.courseselect.entity.CourseSelection;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CourseSelectionService {
    void selectCourse(Long studentId, Long courseId);
    void dropCourse(Long studentId, Long courseId);
    List<Map<String, Object>> getStudentSchedule(Long studentId);
    List<Map<String, Object>> getStudentGrades(Long studentId);
    void setScore(Long teacherId, Long studentId, Long courseId, BigDecimal score);
    List<Map<String, Object>> getCourseStudents(Long courseId);
}
