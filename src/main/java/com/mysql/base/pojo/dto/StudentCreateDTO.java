package com.mysql.base.pojo.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 创建学生DTO
 * 
 * @author zhang
 * @since 2026-04-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生姓名
     */
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 学生年龄
     */
    @NotNull(message = "年龄不能为空")
    @Min(value = 1, message = "年龄必须大于0")
    @Max(value = 150, message = "年龄必须小于150")
    private Integer age;
}
