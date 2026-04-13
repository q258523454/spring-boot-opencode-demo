package com.mysql.base.serivce;

import com.mysql.base.converter.StudentConverter;
import com.mysql.base.pojo.dto.StudentCreateDTO;
import com.mysql.base.pojo.entity.Student;
import com.mysql.base.pojo.vo.StudentVO;
import com.mysql.base.repository.StudentMapper;
import com.mysql.base.serivce.impl.StudentServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * StudentService单元测试类
 * 
 * @author zhang
 * @since 2026-04-13
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentMapper studentMapper;

    @Mock
    private StudentConverter studentConverter;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RBucket<Object> rbucket;

    @BeforeEach
    public void setUp() {
        // 每个测试方法执行前的初始化
    }

    @Test
    void createStudent_shouldReturnStudentVO() {
        // given
        StudentCreateDTO dto = new StudentCreateDTO();

        Student student = new Student();
        student.setId(1);
        student.setName("Test Student");
        student.setAge(20);

        StudentVO vo = new StudentVO();
        vo.setId(1);
        vo.setName("Test Student");
        vo.setAge(20);

        when(studentConverter.createDTOToEntity(any(StudentCreateDTO.class))).thenReturn(student);
        when(studentMapper.insert(any(Student.class))).thenReturn(1);
        when(studentConverter.entityToVO(any(Student.class))).thenReturn(vo);

        // when
        StudentVO result = studentService.createStudent(dto);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getName()).isEqualTo("Test Student");
        assertThat(result.getAge()).isEqualTo(20);

        verify(studentMapper, times(1)).insert(any(Student.class));
    }

    @Test
    void getStudentById_shouldReturnStudentVOFromDB() {
        // given
        Integer id = 1;
        Student student = new Student();
        student.setId(id);
        student.setName("Test Student");
        student.setAge(20);

        StudentVO vo = new StudentVO();
        vo.setId(id);
        vo.setName("Test Student");
        vo.setAge(20);

        when(redissonClient.getBucket(anyString())).thenReturn(rbucket);
        doReturn(null).when(rbucket).get();
        when(studentMapper.selectById(id)).thenReturn(student);
        when(studentConverter.entityToVO(any(Student.class))).thenReturn(vo);

        // when
        StudentVO result = studentService.getStudentById(id);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(id);

        verify(studentMapper, times(1)).selectById(id);
    }
}
