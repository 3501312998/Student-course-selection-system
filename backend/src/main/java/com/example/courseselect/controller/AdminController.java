package com.example.courseselect.controller;

/**
 * 管理员控制器
 * 处理用户管理、课程查询管理等后台管理功能
 */

import com.example.courseselect.common.Result;
import com.example.courseselect.entity.User;
import com.example.courseselect.service.CourseSelectionService;
import com.example.courseselect.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

        private final UserService userService; // 用户服务
        private final CourseSelectionService courseSelectionService; // 选课服务

    @GetMapping("/users")
        /** 用户列表（可选按角色筛选） */
    public Result<List<User>> listUsers(@RequestParam(required = false) String role) {
        return Result.success(userService.listUsers(role));
    }

    @PutMapping("/users/{id}/toggle-status")
        /** 启用/禁用用户 */
    public Result<Void> toggleUserStatus(@PathVariable Long id) {
        userService.toggleUserStatus(id);
        return Result.success("操作成功", null);
    }

    @GetMapping("/courses/{courseId}/students")
        /** 查询某课程所有学生 */
    public Result<List<Map<String, Object>>> getCourseStudents(@PathVariable Long courseId) {
        return Result.success(courseSelectionService.getCourseStudents(courseId));
    }
}
