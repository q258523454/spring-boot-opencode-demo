package com.mysql.base.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.mysql.base.pojo.dto.StudentCreateDTO;
import com.mysql.base.pojo.dto.StudentQueryDTO;
import com.mysql.base.pojo.dto.StudentUpdateDTO;
import com.mysql.base.pojo.vo.StudentVO;
import com.mysql.base.serivce.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * Student控制器
 * 
 * @author zhang
 * @since 2026-04-13
 */
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Slf4j
public class StudentController {

    private final StudentService studentService;

    /**
     * 创建学生
     */
    @PostMapping("/create")
    public StudentVO createStudent(@Valid @RequestBody StudentCreateDTO dto) {
        log.info("创建学生请求: {}", dto);
        return studentService.createStudent(dto);
    }

    /**
     * 根据ID获取学生
     */
    @GetMapping("/{id}")
    public StudentVO getStudent(@PathVariable Integer id) {
        log.info("查询学生请求，ID: {}", id);
        return studentService.getStudentById(id);
    }

    /**
     * 更新学生信息
     */
    @PutMapping("/{id}")
    public StudentVO updateStudent(@PathVariable Integer id, @Valid @RequestBody StudentUpdateDTO dto) {
        dto.setId(id);
        log.info("更新学生请求，ID: {}", id);
        return studentService.updateStudent(dto);
    }

    /**
     * 删除学生
     */
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Integer id) {
        log.info("删除学生请求，ID: {}", id);
        studentService.deleteStudent(id);
    }

    /**
     * 分页查询学生列表
     */
    @GetMapping("/list")
    public IPage<StudentVO> getStudentList(StudentQueryDTO queryDTO) {
        log.info("分页查询学生列表请求: {}", queryDTO);
        return studentService.getStudentList(queryDTO);
    }
}
