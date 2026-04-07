package com.mysql.base.controller;

import com.alibaba.fastjson.JSON;
import com.mysql.base.dao.StudentMapper;
import com.mysql.base.entity.MyResult;
import com.mysql.base.entity.Student;
import com.mysql.base.serivce.IStudentService;
import com.mysql.base.util.MybatisSqlUtil;

import lombok.extern.slf4j.Slf4j;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Slf4j
@RestController
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private IStudentService studentService;
    @Autowired
    private SqlSession sqlSession;

    @GetMapping(value = "/selectAll")
    public String selectAll() {
        String sql = MybatisSqlUtil.showSql(sqlSession, StudentMapper.class, "selectAll", new Object[]{""});
        log.warn(sql);
        String sql2 = MybatisSqlUtil.showSql(sqlSession, StudentMapper.class, "insert", new Object[]{""});
        log.warn(sql2);
        String sql3 = MybatisSqlUtil.showSql(sqlSession, StudentMapper.class, "updateByName", new Object[]{new Student(), ""});
        log.warn(sql3);
        return JSON.toJSONString(studentService.selectAll());
    }

    @GetMapping(value = "/selectAllReturnMap")
    public String selectAllReturnMap() {
        List<Map<Integer, String>> maps = studentService.selectAllReturnMap();
        return JSON.toJSONString(maps);
    }

    @GetMapping(value = "/selectAllReturnSelfObject")
    public String selectAllReturnSelfObject() {
        List<MyResult> myResults = studentService.selectAllReturnSelfObject();
        return JSON.toJSONString(myResults);
    }

    @GetMapping(value = "/selectWhereTest")
    public String selectWhereTest() {

        /*
         * SELECT
         *     id,
         *     name,
         *     age
         * FROM
         *     student
         * WHERE
         *     age <= null;
         */
        // where里面，没有 < if>标签的语句参数, 一定会执行 ,不需要额外加逗号
        List<Student> error = studentService.selectWhereTest(null, "", null);

        /*
         * SELECT
         *     id,
         *     name,
         *     age
         * FROM
         *     student
         * WHERE
         *     age <= 100
         *     and name like CONCAT('%', 'zhang', '%')
         *     and id in (
         *         '0' , '1' , '2' , '3'
         *     );
         */
        List<Student> studentList4 = studentService.selectWhereTest(100, "zhang", Arrays.asList("0", "1", "2", "3"));
        log.info(JSON.toJSONString(studentList4));

        return JSON.toJSONString(studentList4);
    }


    @GetMapping(value = "/insert")
    public String insert(String id) throws InterruptedException {
        Student student = getStudent("");
        return JSON.toJSONString(studentService.insert(student));
    }

    /**
     * 插入后,返回自增id
     */
    @GetMapping(value = "/insert/return/id")
    public String returnId(String id) throws InterruptedException {
        Student student = new Student();
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(Integer.valueOf(("1")));
        log.info("插入前:" + JSON.toJSONString(student));
        int insert = studentService.insert(student);
        log.info("插入后:" + JSON.toJSONString(student));
        return "" + insert;
    }

    @GetMapping(value = "/update")
    public String update(String name) {
        Student student = new Student();
        student.setName(name);
        student.setAge(99);
        return JSON.toJSONString(studentService.updateByName(student, name));
    }


    public Student getStudent(String id) {
        String wId = "";
        if (null != id && !id.isEmpty()) {
            wId = id;
        } else {
            wId = String.valueOf(studentService.selectMaxId() + 1);
        }
        Student student = new Student();
        student.setId(Integer.valueOf(wId));
        student.setName(UUID.randomUUID().toString().substring(0, 3));
        student.setAge(Integer.valueOf(("20")));
        return student;
    }
}
