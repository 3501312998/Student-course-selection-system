package com.example.courseselect.service;

/**
 * 课程服务接口
 * 定义课程增删改查、分页搜索、状态切换等业务方法
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.courseselect.dto.CourseDTO;
import com.example.courseselect.entity.Course;

import java.util.List;

public interface CourseService {
    IPage<Course> listCourses(int page, int size, String keyword, String semester);
    Course getById(Long id);
    Course createCourse(CourseDTO courseDTO);
    Course updateCourse(Long id, CourseDTO courseDTO);
    void deleteCourse(Long id);
    void toggleCourseStatus(Long id);
    List<Course> getTeacherCourses(Long teacherId);
    List<Course> getStudentCourses(Long studentId);
}
