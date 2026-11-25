package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.ExamResultDao;
import com.School_Auto.demo.entity.ExamResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ExamResultServiceImpl implements ExamResultService {

    private ExamResultDao examResultDao;

    @Autowired
    public ExamResultServiceImpl(ExamResultDao examResultDao) {
        this.examResultDao = examResultDao;
    }

    @Override
    @Transactional
    public List<ExamResult> findAll() {
        return examResultDao.findAll();
    }

    @Override
    @Transactional
    public Optional<ExamResult> findById(int id) {
        return Optional.ofNullable(examResultDao.findById(id));
    }

    @Override
    @Transactional
    public void save(ExamResult examResult) {
        examResultDao.save(examResult);
    }

    @Override
    @Transactional
    public void update(ExamResult examResult) {
        examResultDao.update(examResult);
    }

    @Override
    @Transactional
    public void delete(int id) {
        examResultDao.delete(id);
    }

    @Override
    @Transactional
    public List<ExamResult> findByStudentId(int studentId) {
        return examResultDao.findByStudentId(studentId);
    }

    @Override
    @Transactional
    public List<ExamResult> findByExamId(int examId) {
        return examResultDao.findByExamId(examId);
    }

    @Override
    @Transactional
    public void deleteByExamId(int examId) {
        examResultDao.deleteByExamId(examId);
    }

    @Override
    @Transactional
    public void deleteByStudentId(int studentId) {
        examResultDao.deleteByStudentId(studentId);
    }
}
