package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Student;
import java.util.List;
import java.util.Optional;

public interface StudentDao {

    Student save(Student student);  // create or update

    Optional<Student> findById(Long studentId);  // read by PK

    List<Student> findAll();  // read all

    void delete(Long studentId);  // delete

    Optional<Student> findByStudentNo(int studentNo);  // custom query

    List<Student> findByLessonId(int lessonId);  // custom query

    List<Student> findByPersonId(int personId);  // custom query
    
    List<Student> findBySchoolId(int schoolId);  // filter by school
}
