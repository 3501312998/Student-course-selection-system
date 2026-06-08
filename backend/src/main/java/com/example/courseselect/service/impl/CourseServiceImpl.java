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
public class CourseServiceImpl implements CourseService { // 课程服务实现

        private final CourseMapper courseMapper; // 课程数据访问
        private final CourseSelectionMapper courseSelectionMapper; // 选课记录数据访问

    @Override
        /** 课程分页搜索：支持按课程名/编号模糊搜索，按学期筛选 */
    public IPage<Course> listCourses(int page, int size, String keyword, String semester) {
                Page<Course> pageParam = new Page<>(page, size); // MyBatis-Plus 分页对象
                // 条件构造：关键词模糊匹配课程名或编号，学期精确匹配
                .like(StringUtils.hasText(keyword), Course::getCourseName, keyword)
                .or(StringUtils.hasText(keyword), w -> w
                        .like(Course::getCourseCode, keyword))
                .eq(StringUtils.hasText(semester), Course::getSemester, semester)
                .orderByDesc(Course::getCreateTime);
                return courseMapper.selectPage(pageParam, wrapper); // 执行分页查询
    }

    @Override
        /** 根据 ID 查询课程，不存在则抛异常 */
    public Course getById(Long id) {
        Course course = courseMapper.selectById(id);
                if (course == null) {
                        throw new BusinessException("课程不存在");
        }
        return course;
    }

    @Override
        /** 创建课程 */
    public Course createCourse(CourseDTO courseDTO) {
        Course course = Course.builder()
                .courseName(courseDTO.getCourseName())
                .courseCode(courseDTO.getCourseCode())
                .teacherId(courseDTO.getTeacherId())
                .credit(courseDTO.getCredit())
                .maxCapacity(courseDTO.getMaxCapacity())
                                .currentCount(0) // 新建课程已选人数为 0
                .semester(courseDTO.getSemester())
                .schedule(courseDTO.getSchedule())
                .classroom(courseDTO.getClassroom())
                .description(courseDTO.getDescription())
                                .status(0) // 默认关闭
                .build();
                courseMapper.insert(course); // 保存课程
        return course;
    }

    @Override
        /** 更新课程信息 */
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
        /** 逻辑删除课程（MyBatis-Plus @TableLogic 自动处理） */
    public void deleteCourse(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
        courseMapper.deleteById(id);
    }

    @Override
        /** 切换课程开放/关闭状态 */
    public void toggleCourseStatus(Long id) {
        Course course = courseMapper.selectById(id);
        if (course == null) {
            throw new BusinessException("课程不存在");
        }
                course.setStatus(course.getStatus() == 1 ? 0 : 1); // 开放<-->关闭
        courseMapper.updateById(course);
    }

    @Override
        /** 查询某教师的所有课程 */
    public List<Course> getTeacherCourses(Long teacherId) {
        return courseMapper.findByTeacherId(teacherId);
    }

    @Override
        /** 查询学生已选课程列表 */
    public List<Course> getStudentCourses(Long studentId) {
                List<CourseSelection> selections = courseSelectionMapper.findSelectedByStudentId(studentId); // 查询选课记录
                // 提取课程 ID 列表
                .map(CourseSelection::getCourseId)
                .collect(Collectors.toList());
        if (courseIds.isEmpty()) {
            return List.of();
        }
                return courseMapper.selectBatchIds(courseIds); // 批量查询课程详情
    }
}
