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

    private final CourseSelectionService courseSelectionService;
    private final CourseService courseService;
    private final UserService userService;

    @PostMapping("/select/{courseId}")
    public Result<Void> selectCourse(@PathVariable Long courseId, Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        courseSelectionService.selectCourse(securityUser.getId(), courseId);
        return Result.success("选课成功", null);
    }

    @PostMapping("/drop/{courseId}")
    public Result<Void> dropCourse(@PathVariable Long courseId, Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        courseSelectionService.dropCourse(securityUser.getId(), courseId);
        return Result.success("退课成功", null);
    }

    @GetMapping("/schedule")
    public Result<List<Map<String, Object>>> getSchedule(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return Result.success(courseSelectionService.getStudentSchedule(securityUser.getId()));
    }

    @GetMapping("/my-courses")
    public Result<?> getMyCourses(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        User user = userService.getById(securityUser.getId());
        if ("TEACHER".equals(user.getRole())) {
            return Result.success(courseService.getTeacherCourses(user.getId()));
        }
        return Result.success(courseService.getStudentCourses(user.getId()));
    }

    @GetMapping("/grades")
    public Result<List<Map<String, Object>>> getGrades(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return Result.success(courseSelectionService.getStudentGrades(securityUser.getId()));
    }
}
