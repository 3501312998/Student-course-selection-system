/**
 * 业务异常类
 * 用于在 Service 层抛出可携带自定义状态码的业务逻辑异常
 */

package com.example.courseselect.exception;

public class BusinessException extends RuntimeException { // 运行时业务异常
        private final int code;     // 自定义业务状态码

        /** 构造业务异常（带自定义状态码） */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

        /** 构造业务异常（默认 500 状态码） */
    public BusinessException(String message) {
        super(message);
                this.code = 500;          // 默认状态码为 500
    }

    public int getCode() {
        return code;
    }
}
