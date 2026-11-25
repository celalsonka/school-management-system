package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Teacher;
import java.util.List;
import java.util.Optional;

public interface TeacherDao {

    Teacher save(Teacher teacher);  // create or update

    Optional<Teacher> findById(Long id);  // read by PK

    List<Teacher> findAll();  // read all

    void delete(Long id);  // delete

    Optional<Teacher> findByEmployeeNumber(String employeeNumber);  // custom query
}
