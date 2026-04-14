package com.mysql.base.serivce;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.mysql.base.pojo.bo.StudentBO;
import com.mysql.base.pojo.dto.StudentCreateDTO;
import com.mysql.base.pojo.dto.StudentQueryDTO;
import com.mysql.base.pojo.dto.StudentUpdateDTO;
import com.mysql.base.pojo.entity.Student;
import com.mysql.base.pojo.vo.StudentVO;

/**
 * StudentService接口
 * 
 * @author zhang
 * @since 2026-04-13
 */
public interface StudentService extends IService<Student> {

    /**
     * 创建学生
     * 
     * @param dto 创建学生DTO
     * @return 学生VO
     */
    StudentVO createStudent(StudentCreateDTO dto);

    /**
     * 根据ID获取学生
     * 
     * @param id 学生ID
     * @return 学生VO
     */
    StudentVO getStudentById(Integer id);

    /**
     * 更新学生信息
     * 
     * @param dto 更新学生DTO
     * @return 学生VO
     */
    StudentVO updateStudent(StudentUpdateDTO dto);

    /**
     * 删除学生
     * 
     * @param id 学生ID
     */
    void deleteStudent(Integer id);

    /**
     * 分页查询学生列表
     * 
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    IPage<StudentVO> getStudentList(StudentQueryDTO queryDTO);

    /**
     * 根据ID获取学生BO
     * 
     * @param id 学生ID
     * @return 学生BO
     */
    StudentBO getStudentBOById(Integer id);
}
