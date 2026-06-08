package com.example.courseselect.controller;

/**
 * 仪表盘控制器
 * 为首页提供按角色区分的统计数据
 */

import com.example.courseselect.common.Result;
import com.example.courseselect.mapper.CourseMapper;
import com.example.courseselect.mapper.CourseSelectionMapper;
import com.example.courseselect.mapper.UserMapper;
import com.example.courseselect.security.SecurityUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final UserMapper userMapper;
    private final CourseMapper courseMapper;
    private final CourseSelectionMapper courseSelectionMapper;

    @GetMapping("/stats")
    public Result<Map<String, Object>> getStats(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Map<String, Object> stats = new HashMap<>();

        switch (securityUser.getRole()) {
            case "STUDENT":
                Long studentId = securityUser.getId();
                stats.put("selectedCourseCount", courseSelectionMapper.countSelectedByStudentId(studentId));
                stats.put("totalCredits", courseSelectionMapper.sumCreditsByStudentId(studentId));
                stats.put("totalCourses", courseMapper.countAll());
                break;
            case "TEACHER":
                Long teacherId = securityUser.getId();
                stats.put("courseCount", courseMapper.countByTeacherId(teacherId));
                stats.put("studentCount", courseSelectionMapper.countStudentsByTeacherId(teacherId));
                stats.put("ungradedCount", courseSelectionMapper.countUngradedByTeacherId(teacherId));
                break;
            case "ADMIN":
                stats.put("totalUsers", userMapper.countAll());
                stats.put("studentCount", userMapper.countByRole("STUDENT"));
                stats.put("teacherCount", userMapper.countByRole("TEACHER"));
                stats.put("adminCount", userMapper.countByRole("ADMIN"));
                stats.put("totalCourses", courseMapper.countAll());
                stats.put("totalSelections", courseSelectionMapper.countAll());
                break;
        }

        return Result.success(stats);
    }
}
