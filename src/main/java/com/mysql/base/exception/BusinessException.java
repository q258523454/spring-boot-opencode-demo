package com.mysql.base.exception;

import lombok.Getter;

/**
 * 业务异常
 * 
 * @author zhang
 * @since 2026-04-13
 */
@Getter
public class BusinessException extends RuntimeException {

    private Integer code;

    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
