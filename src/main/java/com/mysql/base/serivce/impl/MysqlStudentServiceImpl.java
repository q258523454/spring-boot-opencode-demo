package com.mysql.base.serivce.impl;

import com.mysql.base.dao.StudentMapper;
import com.mysql.base.entity.MyResult;
import com.mysql.base.entity.Student;
import com.mysql.base.serivce.IStudentService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


@Service
public class MysqlStudentServiceImpl implements IStudentService {
    private static final Logger logger = LoggerFactory.getLogger(MysqlStudentServiceImpl.class);

    @Autowired
    protected StudentMapper studentMapper;

    @Override
    public List<Student> selectAll() {
        return studentMapper.selectAll();
    }

    @Override
    public int selectMaxId() {
        return studentMapper.selectMaxId();
    }

    @Override
    @Transactional(timeout = 3)
    public int insert(Student record) {
        return studentMapper.insert(record);
    }

    @Override
    public Student selectByPrimaryKey(Integer id) {
        return studentMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByName(Student updated, String name) {
        return studentMapper.updateByName(updated, name);
    }

    @Override
    public List<Map<Integer, String>> selectAllReturnMap() {
        return studentMapper.selectAllReturnMap();
    }

    @Override
    public List<MyResult> selectAllReturnSelfObject() {
        return studentMapper.selectAllReturnSelfObject();
    }

    @Override
    public List<Student> selectWhereTest(Integer maxAge, String searchName, List<String> idList) {
        return studentMapper.selectWhereTest(maxAge, searchName, idList);
    }

    @Override
    public void createStudentTable() {
        studentMapper.createStudentTable();
    }

    @Override
    public void deleteStudentTable() {
        studentMapper.deleteStudentTable();

    }

    @Override
    public void addStudentTableCol() {
        studentMapper.addStudentTableCol();
    }

    @Override
    public void delStudentTableCol() {
        studentMapper.delStudentTableCol();
    }
}
