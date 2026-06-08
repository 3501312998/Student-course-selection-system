package com.example.courseselect.common;

/**
 * 统一响应结果封装类
 * 所有 API 接口统一使用该类返回，格式为 {code, message, data}
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {
        private int code;                     // 状态码，200 成功，其他表示失败
        private String message;                // 提示信息
        private T data;                        // 泛型数据体

        /** 操作成功（默认提示） */
    public static <T> Result<T> success(T data) {
                return new Result<>(200, "success", data); // 返回成功结果
    }

        /** 操作成功（自定义提示） */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

        /** 操作失败（自定义状态码） */
    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

        /** 操作失败（默认 500 服务器错误） */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

        /** 未登录 / 令牌过期（401） */
    public static <T> Result<T> unauthorized(String message) {
                return new Result<>(401, message, null); // 401 未授权
    }

        /** 无权限访问（403） */
    public static <T> Result<T> forbidden(String message) {
                return new Result<>(403, message, null); // 403 禁止访问
    }
}
