package com.example.courseselect.service;

/**
 * 课程服务接口
 * 定义课程增删改查、分页搜索、状态切换等业务方法
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.courseselect.dto.CourseDTO;
import com.example.courseselect.entity.Course;

import java.util.List;

public interface CourseService { // 课程服务接口
        IPage<Course> listCourses(int page, int size, String keyword, String semester); // 课程分页搜索
        Course getById(Long id); // 根据 ID 查询课程
        Course createCourse(CourseDTO courseDTO); // 创建课程
        Course updateCourse(Long id, CourseDTO courseDTO); // 更新课程
        void deleteCourse(Long id); // 删除课程
        void toggleCourseStatus(Long id); // 切换课程开放/关闭状态
        List<Course> getTeacherCourses(Long teacherId); // 查询教师的课程列表
        List<Course> getStudentCourses(Long studentId); // 查询学生已选课程列表
}
