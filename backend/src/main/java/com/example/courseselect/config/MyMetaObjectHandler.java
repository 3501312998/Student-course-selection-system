package com.example.courseselect.config;

/**
 * MyBatis-Plus 自动填充处理器
 * 自动为实体类的 createTime、updateTime 字段注入当前时间
 */

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component // MyBatis-Plus 自动填充处理器
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
        /** 插入时自动填充创建时间和更新时间 */
    public void insertFill(MetaObject metaObject) {
                this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now()); // 填充创建时间
                this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now()); // 填充更新时间
    }

    @Override
        /** 更新时自动填充更新时间 */
    public void updateFill(MetaObject metaObject) {
                this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now()); // 刷新更新时间
    }
}
