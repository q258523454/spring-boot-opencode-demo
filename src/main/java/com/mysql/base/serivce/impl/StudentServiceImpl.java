package com.mysql.base.serivce.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mysql.base.constant.CacheConstants;
import com.mysql.base.converter.StudentConverter;
import com.mysql.base.pojo.bo.StudentBO;
import com.mysql.base.pojo.dto.StudentCreateDTO;
import com.mysql.base.pojo.dto.StudentQueryDTO;
import com.mysql.base.pojo.dto.StudentUpdateDTO;
import com.mysql.base.pojo.entity.Student;
import com.mysql.base.pojo.vo.StudentVO;
import com.mysql.base.repository.StudentMapper;
import com.mysql.base.serivce.StudentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

/**
 * StudentService实现类
 *
 * @author zhang
 * @since 2026-04-13
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    private final StudentMapper studentMapper;
    private final StudentConverter studentConverter;
    private final RedissonClient redissonClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentVO createStudent(StudentCreateDTO dto) {
        Student student = studentConverter.createDTOToEntity(dto);
        log.info("待插入数据:" + JSON.toJSONString(student));
        studentMapper.insert(student);
        log.info("创建学生成功，ID: {}", student.getId());
        return studentConverter.entityToVO(student);
    }


    @Override
    public StudentVO getStudentById(Integer id) {
        // 先从缓存获取
        String cacheKey = CacheConstants.STUDENT_INFO_KEY + id;
        RBucket<Student> bucket = redissonClient.getBucket(cacheKey);
        Student student = bucket.get();

        if (student != null) {
            log.info("从缓存获取学生信息，ID: {}", id);
            return studentConverter.entityToVO(student);
        }

        // 缓存未命中，查询数据库
        student = studentMapper.selectById(id);
        if (student == null) {
            log.warn("学生不存在，ID: {}", id);
            return null;
        }

        // 写入缓存
        bucket.set(student, CacheConstants.DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);
        log.info("从数据库获取学生信息并写入缓存，ID: {}", id);

        return studentConverter.entityToVO(student);
    }

    @Override
    public StudentBO getStudentBOById(Integer id) {
        StudentVO studentVO = getStudentById(id);
        if (studentVO == null) {
            return null;
        }
        StudentBO bo = new StudentBO();
        bo.setId(studentVO.getId());
        bo.setName(studentVO.getName());
        bo.setAge(studentVO.getAge());
        return bo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StudentVO updateStudent(StudentUpdateDTO dto) {
        Student student = studentMapper.selectById(dto.getId());
        if (student == null) {
            log.warn("学生不存在，无法更新，ID: {}", dto.getId());
            throw new RuntimeException("学生不存在");
        }

        if (StringUtils.hasText(dto.getName())) {
            student.setName(dto.getName());
        }
        if (dto.getAge() != null) {
            student.setAge(dto.getAge());
        }

        studentMapper.updateById(student);

        // 更新缓存
        String cacheKey = CacheConstants.STUDENT_INFO_KEY + dto.getId();
        RBucket<Student> bucket = redissonClient.getBucket(cacheKey);
        bucket.set(student, CacheConstants.DEFAULT_EXPIRE_TIME, TimeUnit.SECONDS);

        log.info("更新学生信息成功，ID: {}", dto.getId());
        return studentConverter.entityToVO(student);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteStudent(Integer id) {
        Student student = studentMapper.selectById(id);
        if (student == null) {
            log.warn("学生不存在，无法删除，ID: {}", id);
            throw new RuntimeException("学生不存在");
        }

        studentMapper.deleteById(id);

        // 删除缓存
        String cacheKey = CacheConstants.STUDENT_INFO_KEY + id;
        redissonClient.getBucket(cacheKey).delete();

        log.info("删除学生成功，ID: {}", id);
    }

    @Override
    public IPage<StudentVO> getStudentList(StudentQueryDTO queryDTO) {
        Page<Student> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(queryDTO.getName())) {
            queryWrapper.like(Student::getName, queryDTO.getName());
        }
        queryWrapper.orderByDesc(Student::getId);

        IPage<Student> studentPage = studentMapper.selectPage(page, queryWrapper);

        // 转换为VO
        Page<StudentVO> voPage = new Page<>(studentPage.getCurrent(), studentPage.getSize(), studentPage.getTotal());
        voPage.setRecords(studentConverter.entityListToVOList(studentPage.getRecords()));

        return voPage;
    }
}
