package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.ExamDao;
import com.School_Auto.demo.entity.Exam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamServiceImpl implements ExamService {

    private final ExamDao examDao;

    public ExamServiceImpl(ExamDao examDao) {
        this.examDao = examDao;
    }

    @Override
    public Exam save(Exam exam) {
        return examDao.save(exam);
    }

    @Override
    public Optional<Exam> findById(int examId) {
        return examDao.findById(examId);
    }

    @Override
    public List<Exam> findAll() {
        return examDao.findAll();
    }

    @Override
    public void delete(int examId) {
        examDao.delete(examId);
    }

    @Override
    public List<Exam> findByLessonId(int lessonId) {
        return examDao.findByLessonId(lessonId);
    }
}
