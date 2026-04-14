package com.mysql.base.converter;

import com.mysql.base.pojo.bo.StudentBO;
import com.mysql.base.pojo.dto.StudentCreateDTO;
import com.mysql.base.pojo.dto.StudentUpdateDTO;
import com.mysql.base.pojo.entity.Student;
import com.mysql.base.pojo.vo.StudentVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Student对象转换器
 * 
 * @author zhang
 * @since 2026-04-13
 */
@Mapper(componentModel = "spring")
public interface StudentConverter {

    /**
     * DTO转Entity
     */
    Student createDTOToEntity(StudentCreateDTO dto);

    /**
     * Entity转VO
     */
    StudentVO entityToVO(Student entity);

    /**
     * Entity转BO
     */
    StudentBO entityToBO(Student entity);

    /**
     * Entity列表转VO列表
     */
    List<StudentVO> entityListToVOList(List<Student> entityList);

    /**
     * UpdateDTO转Entity（不包含ID）
     */
    Student updateDTOToEntity(StudentUpdateDTO dto);
}
