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

        private final UserMapper userMapper; // 用户数据统计
        private final CourseMapper courseMapper; // 课程数据统计
        private final CourseSelectionMapper courseSelectionMapper; // 选课记录统计

    @GetMapping("/stats")
        /** 返回首页仪表盘统计数据，按角色区分 */
    public Result<Map<String, Object>> getStats(Authentication authentication) {
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        Map<String, Object> stats = new HashMap<>();

                // 根据角色返回不同的统计维度
            case "STUDENT":
                Long studentId = securityUser.getId();
                                stats.put("selectedCourseCount", courseSelectionMapper.countSelectedByStudentId(studentId)); // 已选课程数
                                stats.put("totalCredits", courseSelectionMapper.sumCreditsByStudentId(studentId)); // 总学分
                                stats.put("totalCourses", courseMapper.countAll()); // 系统课程总数
                break;
            case "TEACHER":
                Long teacherId = securityUser.getId();
                                stats.put("courseCount", courseMapper.countByTeacherId(teacherId)); // 授课课程数
                                stats.put("studentCount", courseSelectionMapper.countStudentsByTeacherId(teacherId)); // 选课学生数
                                stats.put("ungradedCount", courseSelectionMapper.countUngradedByTeacherId(teacherId)); // 待录入成绩数
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
