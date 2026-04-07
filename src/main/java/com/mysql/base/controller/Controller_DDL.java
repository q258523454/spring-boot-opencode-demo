package com.mysql.base.controller;


import com.mysql.base.serivce.IStudentService;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class Controller_DDL {

    private static final Logger logger = LoggerFactory.getLogger(Controller_DDL.class);

    @Autowired
    private IStudentService studentService;

    @GetMapping(value = "/createStudentTable")
    public String selectAllReturnMap() {
        studentService.createStudentTable();
        return "ok";
    }

    @GetMapping(value = "/deleteStudentTable")
    public String deleteStudentTable() {
        studentService.deleteStudentTable();
        return "ok";
    }

    @GetMapping(value = "/addStudentTableCol")
    public String addStudentTableCol() {
        studentService.addStudentTableCol();
        return "ok";
    }

    @GetMapping(value = "/delStudentTableCol")
    public String delStudentTableCol() {
        studentService.delStudentTableCol();
        return "ok";
    }
}
