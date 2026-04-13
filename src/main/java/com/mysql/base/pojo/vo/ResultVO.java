package com.mysql.base.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一响应结果
 * 
 * @author zhang
 * @since 2026-04-13
 */
@Data
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 消息
     */
    private String message;

    /**
     * 数据
     */
    private T data;

    /**
     * 成功响应
     */
    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(200);
        result.setMessage("success");
        result.setData(data);
        return result;
    }

    /**
     * 成功响应（无数据）
     */
    public static <T> ResultVO<T> success() {
        return success(null);
    }

    /**
     * 失败响应
     */
    public static <T> ResultVO<T> error(Integer code, String message) {
        ResultVO<T> result = new ResultVO<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
    }

    /**
     * 失败响应（默认错误码500）
     */
    public static <T> ResultVO<T> error(String message) {
        return error(500, message);
    }
}
