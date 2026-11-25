package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Exam;
import java.util.List;
import java.util.Optional;

public interface ExamDao {

    Exam save(Exam exam);  // create or update

    Optional<Exam> findById(int examId);  // read by PK

    List<Exam> findAll();  // read all

    void delete(int examId);  // delete

    List<Exam> findByLessonId(int lessonId);  // custom query by lesson
}
