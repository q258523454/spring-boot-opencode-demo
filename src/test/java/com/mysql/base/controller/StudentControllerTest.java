package com.mysql.base.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.base.pojo.dto.StudentCreateDTO;
import com.mysql.base.pojo.vo.StudentVO;
import com.mysql.base.repository.StudentMapper;
import com.mysql.base.serivce.StudentService;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

/**
 * StudentController测试类
 *
 * @author zhang
 * @since 2026-04-13
 */
@Slf4j
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StudentService service;

    @MockitoBean
    private StudentMapper studentMapper;

    @Test
    void createStudent_shouldReturnCreatedStudent() throws Exception {
        log.info("开始执行create测试.");
        StudentCreateDTO studentCreateDTO = StudentCreateDTO.builder().name("张三").age(18).build();
        StudentVO studentVO = StudentVO.builder().id(99).age(99).name("99").build();

        when(service.createStudent(studentCreateDTO)).thenReturn(studentVO);

        MockHttpServletRequestBuilder param = post("/api/students/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentCreateDTO));
        MvcResult mvcResult = mockMvc.perform(param)
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(studentVO)))
                .andReturn();
        log.info("response is :" + mvcResult.getResponse().getContentAsString());
    }

    @Test
    void getStudent_shouldReturnStudent() {
        // TODO: 实现Controller测试
    }

    @Test
    void deleteStudent_shouldReturnOk() {
        // TODO: 实现Controller测试
    }
}
