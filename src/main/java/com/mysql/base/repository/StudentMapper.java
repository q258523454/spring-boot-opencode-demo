package com.mysql.base.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mysql.base.pojo.entity.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * Student Mapper接口
 * 
 * @author zhang
 * @since 2026-04-13
 */
@Mapper
public interface StudentMapper extends BaseMapper<Student> {

}
