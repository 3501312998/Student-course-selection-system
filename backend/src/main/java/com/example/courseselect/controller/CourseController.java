package com.example.courseselect.controller;

/**
 * 课程查询控制器
 * 提供课程列表查询、详情查看等功能（公开接口，无需登录）
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.courseselect.common.Result;
import com.example.courseselect.entity.Course;
import com.example.courseselect.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

        private final CourseService courseService; // 课程服务

    @GetMapping
        /** 课程分页搜索公开接口 */
    public Result<IPage<Course>> listCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String semester) {
                return Result.success(courseService.listCourses(page, size, keyword, semester)); // 返回分页结果
    }

    @GetMapping("/{id}")
        /** 课程详情 */
    public Result<Course> getById(@PathVariable Long id) {
        return Result.success(courseService.getById(id));
    }

    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
        /** 切换课程状态（教师/管理员） */
    public Result<Void> toggleCourseStatus(@PathVariable Long id) {
        courseService.toggleCourseStatus(id);
        return Result.success("操作成功", null);
    }
}
