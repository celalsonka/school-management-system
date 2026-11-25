package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {

    Student save(Student student);

    Optional<Student> findById(Long studentId);

    List<Student> findAll();

    void delete(Long studentId);

    Optional<Student> findByStudentNo(int studentNo);

    Student update(Student student);

    List<Student> findByLessonId(int lessonId);

    List<Student> findByPersonId(int personId);
    
    List<Student> findBySchoolId(int schoolId);
}
