package com.example.courseselect.controller;

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

    private final UserService userService;
    private final CourseSelectionService courseSelectionService;

    @GetMapping("/users")
    public Result<List<User>> listUsers(@RequestParam(required = false) String role) {
        return Result.success(userService.listUsers(role));
    }

    @PutMapping("/users/{id}/toggle-status")
    public Result<Void> toggleUserStatus(@PathVariable Long id) {
        userService.toggleUserStatus(id);
        return Result.success("操作成功", null);
    }

    @GetMapping("/courses/{courseId}/students")
    public Result<List<Map<String, Object>>> getCourseStudents(@PathVariable Long courseId) {
        return Result.success(courseSelectionService.getCourseStudents(courseId));
    }
}
