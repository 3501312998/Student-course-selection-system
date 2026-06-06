package com.example.courseselect.common;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    CREATED(201, "创建成功"),
    BAD_REQUEST(400, "请求参数错误"),
    UNAUTHORIZED(401, "未登录或Token已过期"),
    FORBIDDEN(403, "无权限访问"),
    NOT_FOUND(404, "资源不存在"),
    CONFLICT(409, "资源冲突"),
    INTERNAL_ERROR(500, "服务器内部错误"),
    COURSE_FULL(4001, "课程已满员"),
    COURSE_CONFLICT(4002, "选课时间冲突"),
    ALREADY_SELECTED(4003, "已选过此课程"),
    PASSWORD_ERROR(4004, "密码错误"),
    USER_DISABLED(4005, "账号已被禁用");

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
