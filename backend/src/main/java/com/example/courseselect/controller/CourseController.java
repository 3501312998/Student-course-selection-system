package com.example.courseselect.controller;

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

    private final CourseService courseService;

    @GetMapping
    public Result<IPage<Course>> listCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String semester) {
        return Result.success(courseService.listCourses(page, size, keyword, semester));
    }

    @GetMapping("/{id}")
    public Result<Course> getById(@PathVariable Long id) {
        return Result.success(courseService.getById(id));
    }

    @PutMapping("/{id}/toggle-status")
    @PreAuthorize("hasAnyRole('ADMIN', 'TEACHER')")
    public Result<Void> toggleCourseStatus(@PathVariable Long id) {
        courseService.toggleCourseStatus(id);
        return Result.success("操作成功", null);
    }
}
