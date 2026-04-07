package com.mysql.base.dao;

import com.mysql.base.entity.MyResult;
import com.mysql.base.entity.Student;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface StudentMapper {
    /**
     * 返回自增id, 赋值给record类
     */
    int insert(Student record);

    Student selectByPrimaryKey(Integer id);

    List<Student> selectAll();

    int selectMaxId();

    int updateByName(@Param("updated") Student updated, @Param("name") String name);

    List<Map<Integer, String>> selectAllReturnMap();

    List<MyResult> selectAllReturnSelfObject();

    List<Student> selectWhereTest(@Param("maxAge") Integer maxAge, @Param("serachName") String serachName, @Param("idList") List<String> idList);

    void createStudentTable();

    void deleteStudentTable();

    void addStudentTableCol();

    void delStudentTableCol();
}