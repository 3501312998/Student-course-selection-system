package com.example.courseselect.controller;

import com.example.courseselect.common.Result;
import com.example.courseselect.dto.CourseDTO;
import com.example.courseselect.entity.Course;
import com.example.courseselect.security.SecurityUser;
import com.example.courseselect.service.CourseService;
import com.example.courseselect.service.CourseSelectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final CourseService courseService;
    private final CourseSelectionService courseSelectionService;

    @GetMapping("/courses")
    public Result<List<Course>> getMyCourses(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return Result.success(courseService.getTeacherCourses(securityUser.getId()));
    }

    @PostMapping("/courses")
    public Result<Course> createCourse(@Valid @RequestBody CourseDTO courseDTO,
                                        Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        courseDTO.setTeacherId(securityUser.getId());
        return Result.success("创建成功", courseService.createCourse(courseDTO));
    }

    @PutMapping("/courses/{id}")
    public Result<Course> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDTO courseDTO) {
        return Result.success("更新成功", courseService.updateCourse(id, courseDTO));
    }

    @GetMapping("/courses/{courseId}/students")
    public Result<List<Map<String, Object>>> getCourseStudents(@PathVariable Long courseId) {
        return Result.success(courseSelectionService.getCourseStudents(courseId));
    }

    @PostMapping("/score")
    public Result<Void> setScore(@RequestBody Map<String, Object> body,
                                  Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long studentId = Long.valueOf(body.get("studentId").toString());
        Long courseId = Long.valueOf(body.get("courseId").toString());
        BigDecimal score = new BigDecimal(body.get("score").toString());
        courseSelectionService.setScore(securityUser.getId(), studentId, courseId, score);
        return Result.success("成绩录入成功", null);
    }
}
