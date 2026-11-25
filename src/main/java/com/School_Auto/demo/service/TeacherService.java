package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherService {

    Teacher save(Teacher teacher);

    Optional<Teacher> findById(Long id);

    List<Teacher> findAll();

    void delete(Long id);

    Optional<Teacher> findByEmployeeNumber(String employeeNumber);
}
