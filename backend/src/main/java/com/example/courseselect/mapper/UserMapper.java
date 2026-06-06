package com.example.courseselect.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.courseselect.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT * FROM t_user WHERE username = #{username} AND deleted = 0")
    User findByUsername(@Param("username") String username);

    @Select("SELECT * FROM t_user WHERE student_no = #{studentNo} AND deleted = 0")
    User findByStudentNo(@Param("studentNo") String studentNo);
}
