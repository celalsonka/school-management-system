package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.ExamResult;
import java.util.List;
import java.util.Optional;

public interface ExamResultService {
    List<ExamResult> findAll();
    Optional<ExamResult> findById(int id);
    void save(ExamResult examResult);
    void update(ExamResult examResult);
    void delete(int id);
    List<ExamResult> findByStudentId(int studentId);
    List<ExamResult> findByExamId(int examId);
    void deleteByExamId(int examId);
    void deleteByStudentId(int studentId);
}
