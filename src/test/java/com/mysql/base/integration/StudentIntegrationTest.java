package com.mysql.base.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.base.pojo.dto.StudentCreateDTO;
import com.mysql.base.pojo.vo.StudentVO;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentIntegrationTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void greet_shouldReturnRealServiceResponse() {
        StudentCreateDTO studentCreateDTO = StudentCreateDTO.builder().name("张三").age(18).build();

        String url = "http://localhost:" + port + "/api/students/create";
        ResponseEntity<StudentVO> response = restTemplate.postForEntity(url, studentCreateDTO, StudentVO.class);
        StudentVO body = response.getBody();
        log.info("body={}", body);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getName()).isEqualTo(studentCreateDTO.getName());
        assertThat(response.getBody().getAge()).isEqualTo(studentCreateDTO.getAge());
    }
}