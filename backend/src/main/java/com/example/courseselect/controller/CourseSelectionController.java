package com.example.courseselect.controller;

/**
 * 选课操作控制器
 * 处理学生的选课、退课、课表、成绩查询等请求
 */

import com.example.courseselect.common.Result;
import com.example.courseselect.entity.User;
import com.example.courseselect.security.SecurityUser;
import com.example.courseselect.service.CourseSelectionService;
import com.example.courseselect.service.CourseService;
import com.example.courseselect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/selection")
@RequiredArgsConstructor
public class CourseSelectionController {

        private final CourseSelectionService courseSelectionService; // 选课服务
        private final CourseService courseService; // 课程服务
        private final UserService userService; // 用户服务

    @PostMapping("/select/{courseId}")
        /** 学生选课 */
    public Result<Void> selectCourse(@PathVariable Long courseId, Authentication authentication) {
                SecurityUser securityUser = (SecurityUser) authentication.getPrincipal(); // 获取当前认证用户
                courseSelectionService.selectCourse(securityUser.getId(), courseId); // 执行选课
                return Result.success("选课成功", null);
    }

    @PostMapping("/drop/{courseId}")
        /** 学生退课 */
    public Result<Void> dropCourse(@PathVariable Long courseId, Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        courseSelectionService.dropCourse(securityUser.getId(), courseId);
        return Result.success("退课成功", null);
    }

    @GetMapping("/schedule")
        /** 学生课表 */
    public Result<List<Map<String, Object>>> getSchedule(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return Result.success(courseSelectionService.getStudentSchedule(securityUser.getId()));
    }

    @GetMapping("/my-courses")
        /** 我的课程（按角色返回教师或学生课程） */
    public Result<?> getMyCourses(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        User user = userService.getById(securityUser.getId());
                if ("TEACHER".equals(user.getRole())) { // 教师返回授课课程
                        return Result.success(courseService.getTeacherCourses(user.getId()));
        }
                return Result.success(courseService.getStudentCourses(user.getId())); // 学生返回已选课程
    }

    @GetMapping("/grades")
        /** 学生成绩查询 */
    public Result<List<Map<String, Object>>> getGrades(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return Result.success(courseSelectionService.getStudentGrades(securityUser.getId()));
    }
}
