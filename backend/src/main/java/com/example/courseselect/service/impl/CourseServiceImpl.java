package com.example.courseselect.service.impl;

/**
 * 课程服务实现类
 * 实现课程的管理操作，包括分页搜索、创建、更新、删除等
 */

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.courseselect.dto.CourseDTO;
import com.example.courseselect.entity.Course;
import com.example.courseselect.entity.CourseSelection;
import com.example.courseselect.exception.BusinessException;
import com.example.courseselect.mapper.CourseMapper;
import com.example.courseselect.mapper.CourseSelectionMapper;
import com.example.courseselect.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseMapper courseMapper;
    private final CourseSelectionMapper courseSelectionMapper;

    @Override
    public IPage<Course> listCourses(int page, int size, String keyword, String semester) {
        Page<Course> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Course> wrapper = new LambdaQueryWrapper<Course>()
                .like(StringUtils.hasText(keyword), Course::getCourseName, keyword)
                .or(StringUtils.hasText(keyword), w -> w
                        .like(Course::getCourseCode, keyword))
                .eq(StringUtils.hasText(semester), Course::getSemester, semester)
                .orderByDesc(Course::getCreateTime);
        return courseMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public Course getById(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        return course;
    }

    @Override
    public Course createCourse(CourseDTO courseDTO) {
        Course course = Course.builder()
                .courseName(courseDTO.getCourseName())
                .courseCode(courseDTO.getCourseCode())
                .teacherId(courseDTO.getTeacherId())
                .credit(courseDTO.getCredit())
                .maxCapacity(courseDTO.getMaxCapacity())
                .currentCount(0)
                .semester(courseDTO.getSemester())
                .schedule(courseDTO.getSchedule())
                .classroom(courseDTO.getClassroom())
                .description(courseDTO.getDescription())
                .status(0)
                .build();
        courseMapper.insert(course);
        return course;
    }

    @Override
    public Course updateCourse(Long id, CourseDTO courseDTO) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        course.setCourseName(courseDTO.getCourseName());
        course.setCredit(courseDTO.getCredit());
        course.setMaxCapacity(courseDTO.getMaxCapacity());
        course.setSchedule(courseDTO.getSchedule());
        course.setClassroom(courseDTO.getClassroom());
        course.setDescription(courseDTO.getDescription());
        courseMapper.updateById(course);
        return course;
    }

    @Override
    public void deleteCourse(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        courseMapper.deleteById(id);
    }

    @Override
    public void toggleCourseStatus(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        course.setStatus(course.getStatus() == 1 ? 0 : 1);
        courseMapper.updateById(course);
    }

    @Override
    public List<Course> getTeacherCourses(Long teacherId) {
        return courseMapper.findByTeacherId(teacherId);
    }

    @Override
    public List<Course> getStudentCourses(Long studentId) {
        List<CourseSelection> selections = courseSelectionMapper.findSelectedByStudentId(studentId);
        List<Long> courseIds = selections.stream()
                .map(CourseSelection::getCourseId)
                .collect(Collectors.toList());
        if (courseIds.isEmpty()) {
            return List.of();
        }
        return courseMapper.selectBatchIds(courseIds);
    }
}
