/**
 * 业务异常类
 * 用于在 Service 层抛出可携带自定义状态码的业务逻辑异常
 */

package com.example.courseselect.exception;

public class BusinessException extends RuntimeException {
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public int getCode() {
        return code;
    }
}
