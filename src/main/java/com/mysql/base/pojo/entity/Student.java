package com.mysql.base.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Student实体类
 * 
 * @author zhang
 * @since 2026-04-13
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("student")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
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
