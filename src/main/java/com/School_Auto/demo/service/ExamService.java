package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.Exam;

import java.util.List;
import java.util.Optional;

public interface ExamService {

    Exam save(Exam exam);

    Optional<Exam> findById(int examId);

    List<Exam> findAll();

    void delete(int examId);

    List<Exam> findByLessonId(int lessonId);
}
