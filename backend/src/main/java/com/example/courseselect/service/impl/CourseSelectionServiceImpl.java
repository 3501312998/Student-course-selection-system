package com.example.courseselect.service.impl;

import com.example.courseselect.common.ResultCode;
import com.example.courseselect.entity.Course;
import com.example.courseselect.entity.CourseSelection;
import com.example.courseselect.exception.BusinessException;
import com.example.courseselect.mapper.CourseMapper;
import com.example.courseselect.mapper.CourseSelectionMapper;
import com.example.courseselect.mapper.UserMapper;
import com.example.courseselect.service.CourseSelectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseSelectionServiceImpl implements CourseSelectionService {

    private final CourseSelectionMapper courseSelectionMapper;
    private final CourseMapper courseMapper;
    private final UserMapper userMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectCourse(Long studentId, Long courseId) {
        Course course = courseMapper.selectById(courseId);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        if (course.getStatus() != 1) {
            throw new BusinessException("课程未开放选课");
        }
        if (course.getCurrentCount() >= course.getMaxCapacity()) {
            throw new BusinessException(ResultCode.COURSE_FULL.getCode(), "课程已满员");
        }

        // 检查是否已选
        CourseSelection existing = courseSelectionMapper.findByStudentAndCourse(studentId, courseId);
        if (existing != null) {
            if (existing.getStatus() == 1) {
                throw new BusinessException(ResultCode.ALREADY_SELECTED.getCode(), "已选过此课程");
            }
            // 如果已退课，重新选课
            existing.setStatus(1);
            existing.setSelectTime(LocalDateTime.now());
            courseSelectionMapper.updateById(existing);
            course.setCurrentCount(course.getCurrentCount() + 1);
            courseMapper.updateById(course);
            return;
        }

        // 检查选课时间冲突
        List<CourseSelection> selected = courseSelectionMapper.findSelectedByStudentId(studentId);
        List<Long> selectedCourseIds = selected.stream()
                .map(CourseSelection::getCourseId)
                .collect(Collectors.toList());
        if (!selectedCourseIds.isEmpty()) {
            List<Course> selectedCourses = courseMapper.selectBatchIds(selectedCourseIds);
            for (Course sc : selectedCourses) {
                if (isTimeConflict(course.getSchedule(), sc.getSchedule())) {
                    throw new BusinessException(ResultCode.COURSE_CONFLICT.getCode(),
                            "与课程《" + sc.getCourseName() + "》时间冲突");
                }
            }
        }

        // 创建选课记录
        CourseSelection selection = CourseSelection.builder()
                .studentId(studentId)
                .courseId(courseId)
                .status(1)
                .selectTime(LocalDateTime.now())
                .build();
        courseSelectionMapper.insert(selection);

        // 更新已选人数
        course.setCurrentCount(course.getCurrentCount() + 1);
        courseMapper.updateById(course);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dropCourse(Long studentId, Long courseId) {
        CourseSelection selection = courseSelectionMapper.findByStudentAndCourse(studentId, courseId);
        if (selection == null || selection.getStatus() != 1) {
            throw new BusinessException("未选此课程");
        }

        selection.setStatus(2);
        courseSelectionMapper.updateById(selection);

        Course course = courseMapper.selectById(courseId);
        if (course != null && course.getCurrentCount() > 0) {
            course.setCurrentCount(course.getCurrentCount() - 1);
            courseMapper.updateById(course);
        }
    }

    @Override
    public List<Map<String, Object>> getStudentSchedule(Long studentId) {
        List<CourseSelection> selections = courseSelectionMapper.findSelectedByStudentId(studentId);
        List<Long> courseIds = selections.stream()
                .map(CourseSelection::getCourseId)
                .collect(Collectors.toList());
        if (courseIds.isEmpty()) return List.of();

        List<Course> courses = courseMapper.selectBatchIds(courseIds);
        List<Map<String, Object>> result = new ArrayList<>();
        for (Course course : courses) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", course.getId());
            item.put("courseName", course.getCourseName());
            item.put("courseCode", course.getCourseCode());
            item.put("schedule", course.getSchedule());
            item.put("classroom", course.getClassroom());
            item.put("teacherName", userMapper.selectById(course.getTeacherId()) != null
                    ? userMapper.selectById(course.getTeacherId()).getRealName() : "");
            item.put("credit", course.getCredit());
            result.add(item);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getStudentGrades(Long studentId) {
        List<CourseSelection> selections = courseSelectionMapper.findAllByStudentId(studentId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (CourseSelection cs : selections) {
            Course course = courseMapper.selectById(cs.getCourseId());
            if (course == null) continue;
            Map<String, Object> item = new HashMap<>();
            item.put("courseName", course.getCourseName());
            item.put("courseCode", course.getCourseCode());
            item.put("credit", course.getCredit());
            item.put("score", cs.getScore());
            item.put("status", cs.getStatus());
            item.put("selectTime", cs.getSelectTime());
            result.add(item);
        }
        return result;
    }

    @Override
    public void setScore(Long teacherId, Long studentId, Long courseId, BigDecimal score) {
        Course course = courseMapper.selectById(courseId);
        if (course == null || !course.getTeacherId().equals(teacherId)) {
            throw new BusinessException("您不是该课程的授课教师");
        }
        CourseSelection selection = courseSelectionMapper.findByStudentAndCourse(studentId, courseId);
        if (selection == null) {
            throw new BusinessException("该学生未选此课程");
        }
        selection.setScore(score);
        selection.setStatus(3);
        courseSelectionMapper.updateById(selection);
    }

    @Override
    public List<Map<String, Object>> getCourseStudents(Long courseId) {
        List<CourseSelection> selections = courseSelectionMapper.findByCourseId(courseId);
        List<Map<String, Object>> result = new ArrayList<>();
        for (CourseSelection cs : selections) {
            var user = userMapper.selectById(cs.getStudentId());
            if (user == null) continue;
            Map<String, Object> item = new HashMap<>();
            item.put("studentId", user.getId());
            item.put("studentNo", user.getStudentNo());
            item.put("studentName", user.getRealName());
            item.put("score", cs.getScore());
            item.put("status", cs.getStatus());
            item.put("selectTime", cs.getSelectTime());
            result.add(item);
        }
        return result;
    }

    private boolean isTimeConflict(String schedule1, String schedule2) {
        if (schedule1 == null || schedule2 == null) return false;
        // 提取星期
        String day1 = schedule1.replaceAll("[\\d\\-\\s节].*", "").trim();
        String day2 = schedule2.replaceAll("[\\d\\-\\s节].*", "").trim();
        if (!day1.equals(day2)) return false;

        // 提取节次范围 (如 "周一 1-2节" → [1,2])
        Pattern p = Pattern.compile("(\\d+)\\s*-\\s*(\\d+)");
        Matcher m1 = p.matcher(schedule1);
        Matcher m2 = p.matcher(schedule2);
        if (m1.find() && m2.find()) {
            int start1 = Integer.parseInt(m1.group(1));
            int end1 = Integer.parseInt(m1.group(2));
            int start2 = Integer.parseInt(m2.group(1));
            int end2 = Integer.parseInt(m2.group(2));
            // 区间重叠检测: [start1, end1] 与 [start2, end2] 有交集
            return Math.max(start1, start2) <= Math.min(end1, end2);
        }
        log.warn("无法解析排课时间格式: schedule1={}, schedule2={}", schedule1, schedule2);
        return false; // 解析失败时返回不冲突，避免误拦截
    }
}
