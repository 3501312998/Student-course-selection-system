-- =====================================================
-- 学生选课系统 — 数据库初始化脚本
-- 技术栈: MySQL 5.7+
-- 密码: 20040203 (BCrypt加密)
-- =====================================================

CREATE DATABASE IF NOT EXISTS course_select DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE course_select;

-- =====================================================
-- 用户表
-- =====================================================
DROP TABLE IF EXISTS t_course_selection;
DROP TABLE IF EXISTS t_course;
DROP TABLE IF EXISTS t_user;

CREATE TABLE t_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(BCrypt加密)',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    email VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    phone VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    role VARCHAR(20) NOT NULL DEFAULT 'STUDENT' COMMENT '角色: ADMIN/TEACHER/STUDENT',
    gender TINYINT DEFAULT 0 COMMENT '性别: 0未知 1男 2女',
    student_no VARCHAR(50) DEFAULT NULL COMMENT '学号/工号',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1启用 0禁用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0未删除 1已删除',
    INDEX idx_role (role),
    INDEX idx_student_no (student_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =====================================================
-- 课程表
-- =====================================================
CREATE TABLE t_course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    course_name VARCHAR(100) NOT NULL COMMENT '课程名称',
    course_code VARCHAR(50) NOT NULL UNIQUE COMMENT '课程编号',
    teacher_id BIGINT NOT NULL COMMENT '授课教师ID',
    credit TINYINT NOT NULL DEFAULT 3 COMMENT '学分',
    max_capacity INT NOT NULL DEFAULT 60 COMMENT '最大容量',
    current_count INT NOT NULL DEFAULT 0 COMMENT '已选人数',
    semester VARCHAR(50) NOT NULL COMMENT '学期, 如 2025-2026-1',
    schedule VARCHAR(100) DEFAULT NULL COMMENT '上课时间, 如 周一 1-2节',
    classroom VARCHAR(100) DEFAULT NULL COMMENT '上课地点',
    description TEXT DEFAULT NULL COMMENT '课程描述',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1开放 0关闭 2已结束',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0未删除 1已删除',
    INDEX idx_teacher (teacher_id),
    INDEX idx_semester (semester),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='课程表';

-- =====================================================
-- 选课记录表
-- =====================================================
CREATE TABLE t_course_selection (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    course_id BIGINT NOT NULL COMMENT '课程ID',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 1已选 2已退 3已结课',
    score DECIMAL(5,2) DEFAULT NULL COMMENT '成绩',
    select_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '选课时间',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0未删除 1已删除',
    UNIQUE KEY uk_student_course (student_id, course_id),
    INDEX idx_student (student_id),
    INDEX idx_course (course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='选课记录表';

-- =====================================================
-- 预置数据
-- =====================================================

-- 密码为 20040203 的 BCrypt 加密值
-- 管理员账号
INSERT INTO t_user (username, password, real_name, role, student_no, status) VALUES
('admin', '$2a$10$U3ziXiOIyzD2s6PnBaaH5OyFTKsB4QxFAecsnR/B9PMysKnK4.OYO', '系统管理员', 'ADMIN', 'A00001', 1);

-- 教师账号 (密码同 20040203)
INSERT INTO t_user (username, password, real_name, role, student_no, status) VALUES
('zhang_san', '$2a$10$U3ziXiOIyzD2s6PnBaaH5OyFTKsB4QxFAecsnR/B9PMysKnK4.OYO', '张三', 'TEACHER', 'T10001', 1),
('li_si', '$2a$10$U3ziXiOIyzD2s6PnBaaH5OyFTKsB4QxFAecsnR/B9PMysKnK4.OYO', '李四', 'TEACHER', 'T10002', 1),
('wang_wu', '$2a$10$U3ziXiOIyzD2s6PnBaaH5OyFTKsB4QxFAecsnR/B9PMysKnK4.OYO', '王五', 'TEACHER', 'T10003', 1);

-- 学生账号 (密码同 20040203)
INSERT INTO t_user (username, password, real_name, role, student_no, status) VALUES
('stu_001', '$2a$10$U3ziXiOIyzD2s6PnBaaH5OyFTKsB4QxFAecsnR/B9PMysKnK4.OYO', '赵六', 'STUDENT', 'S2024001', 1),
('stu_002', '$2a$10$U3ziXiOIyzD2s6PnBaaH5OyFTKsB4QxFAecsnR/B9PMysKnK4.OYO', '钱七', 'STUDENT', 'S2024002', 1),
('stu_003', '$2a$10$U3ziXiOIyzD2s6PnBaaH5OyFTKsB4QxFAecsnR/B9PMysKnK4.OYO', '孙八', 'STUDENT', 'S2024003', 1),
('stu_004', '$2a$10$U3ziXiOIyzD2s6PnBaaH5OyFTKsB4QxFAecsnR/B9PMysKnK4.OYO', '周九', 'STUDENT', 'S2024004', 1),
('stu_005', '$2a$10$U3ziXiOIyzD2s6PnBaaH5OyFTKsB4QxFAecsnR/B9PMysKnK4.OYO', '吴十', 'STUDENT', 'S2024005', 1);

-- 课程数据 (2025-2026-1 学期)
INSERT INTO t_course (course_name, course_code, teacher_id, credit, max_capacity, current_count, semester, schedule, classroom, description, status) VALUES
('Java程序设计', 'CS101', 2, 4, 60, 0, '2025-2026-1', '周一 1-2节', '教学楼A101', 'Java面向对象编程基础与进阶', 1),
('数据结构与算法', 'CS102', 2, 4, 55, 0, '2025-2026-1', '周二 3-4节', '教学楼A102', '常用数据结构与算法设计', 1),
('数据库原理', 'CS103', 3, 3, 50, 0, '2025-2026-1', '周三 1-2节', '教学楼B201', '关系数据库理论与SQL实践', 1),
('操作系统', 'CS104', 3, 4, 60, 0, '2025-2026-1', '周四 5-6节', '教学楼B202', '操作系统核心概念与原理', 1),
('计算机网络', 'CS105', 4, 3, 55, 0, '2025-2026-1', '周五 1-2节', '实验楼C301', 'TCP/IP协议栈与网络编程', 1),
('软件工程', 'CS106', 4, 3, 60, 0, '2025-2026-1', '周一 5-6节', '教学楼A201', '软件开发流程与项目管理', 1),
('Web前端开发', 'CS107', 2, 3, 50, 0, '2025-2026-1', '周二 1-2节', '实验楼C302', 'HTML/CSS/JavaScript与Vue框架', 1),
('人工智能导论', 'CS108', 3, 3, 45, 0, '2025-2026-1', '周三 3-4节', '教学楼A301', 'AI基础概念与机器学习入门', 1);
