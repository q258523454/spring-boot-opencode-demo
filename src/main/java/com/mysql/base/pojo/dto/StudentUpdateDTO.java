package com.mysql.base.pojo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 更新学生DTO
 * 
 * @author zhang
 * @since 2026-04-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateDTO implements Serializable {

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
    @Min(value = 1, message = "年龄必须大于0")
    @Max(value = 150, message = "年龄必须小于150")
    private Integer age;
}
