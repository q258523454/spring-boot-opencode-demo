package com.mysql.base.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 学生VO
 * 
 * @author zhang
 * @since 2026-04-13
 */
@Data
public class StudentVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Integer id;

    /**
     * 学生姓名
     */
    private String name;

    /**
     * 学生年龄
     */
    private Integer age;
}
