package com.example.courseselect.mapper;

/**
 * 用户数据访问层
 * 定义 User 实体的数据库操作接口，提供按用户名/学号查询
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.courseselect.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> { // 用户数据访问

    @Select("SELECT COUNT(*) FROM t_user WHERE deleted = 0")
        long countAll(); // 统计用户总数（未删除）

    @Select("SELECT COUNT(*) FROM t_user WHERE role = #{role} AND deleted = 0")
        long countByRole(@Param("role") String role); // 按角色统计用户数

    @Select("SELECT * FROM t_user WHERE username = #{username} AND deleted = 0")
        User findByUsername(@Param("username") String username); // 按用户名查询

    @Select("SELECT * FROM t_user WHERE student_no = #{studentNo} AND deleted = 0")
        User findByStudentNo(@Param("studentNo") String studentNo); // 按学号查询
}
