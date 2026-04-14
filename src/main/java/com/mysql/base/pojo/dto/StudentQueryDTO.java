package com.mysql.base.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 查询学生DTO
 * 
 * @author zhang
 * @since 2026-04-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentQueryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生姓名（模糊查询）
     */
    private String name;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;
}
