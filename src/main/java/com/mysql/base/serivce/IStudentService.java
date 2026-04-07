package com.mysql.base.serivce;


import com.mysql.base.entity.MyResult;
import com.mysql.base.entity.Student;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @date 2020-03-12 22:01
 * @modify
 */
public interface IStudentService {

    List<Student> selectAll();

    int selectMaxId();

    int insert(Student record);

    Student selectByPrimaryKey(Integer id);

    int updateByName(Student updated, String name);

    List<Map<Integer, String>> selectAllReturnMap();

    List<MyResult> selectAllReturnSelfObject();

    List<Student> selectWhereTest(Integer maxAge, String searchName, List<String> idList);

    void createStudentTable();

    void deleteStudentTable();

    void addStudentTableCol();

    void delStudentTableCol();
}
