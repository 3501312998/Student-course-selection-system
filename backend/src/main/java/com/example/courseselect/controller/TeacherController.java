package com.example.courseselect.controller;

/**
 * 教师控制器
 * 处理教师管理课程、查看学生名单、录入成绩等请求
 */

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

        private final CourseService courseService; // 课程服务
        private final CourseSelectionService courseSelectionService; // 选课服务

    @GetMapping("/courses")
        /** 教师查看自己的课程列表 */
    public Result<List<Course>> getMyCourses(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        return Result.success(courseService.getTeacherCourses(securityUser.getId()));
    }

    @PostMapping("/courses")
        /** 教师创建新课程 */
    public Result<Course> createCourse(@Valid @RequestBody CourseDTO courseDTO,
                                        Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
                courseDTO.setTeacherId(securityUser.getId()); // 绑定当前教师为授课教师
        return Result.success("创建成功", courseService.createCourse(courseDTO));
    }

    @PutMapping("/courses/{id}")
        /** 更新课程信息 */
    public Result<Course> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDTO courseDTO) {
        return Result.success("更新成功", courseService.updateCourse(id, courseDTO));
    }

    @GetMapping("/courses/{courseId}/students")
        /** 查看某课程的学生名单 */
    public Result<List<Map<String, Object>>> getCourseStudents(@PathVariable Long courseId) {
        return Result.success(courseSelectionService.getCourseStudents(courseId));
    }

    @PostMapping("/score")
        /** 录入/修改成绩 */
    public Result<Void> setScore(@RequestBody Map<String, Object> body,
                                  Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Long studentId = Long.valueOf(body.get("studentId").toString());
        Long courseId = Long.valueOf(body.get("courseId").toString());
                BigDecimal score = new BigDecimal(body.get("score").toString()); // 解析成绩数值
                courseSelectionService.setScore(securityUser.getId(), studentId, courseId, score); // 执行成绩录入
        return Result.success("成绩录入成功", null);
    }
}
