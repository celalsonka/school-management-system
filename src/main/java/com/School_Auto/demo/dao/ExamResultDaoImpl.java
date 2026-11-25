package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.ExamResult;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExamResultDaoImpl implements ExamResultDao {

    private EntityManager entityManager;

    @Autowired
    public ExamResultDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ExamResult> findAll() {
        TypedQuery<ExamResult> theQuery = entityManager.createQuery("FROM ExamResult", ExamResult.class);
        return theQuery.getResultList();
    }

    @Override
    public ExamResult findById(int id) {
        return entityManager.find(ExamResult.class, id);
    }

    @Override
    public void save(ExamResult examResult) {
        entityManager.persist(examResult);
    }

    @Override
    public void update(ExamResult examResult) {
        entityManager.merge(examResult);
    }

    @Override
    public void delete(int id) {
        ExamResult examResult = findById(id);
        if (examResult != null) {
            entityManager.remove(examResult);
        }
    }

    @Override
    public List<ExamResult> findByStudentId(int studentId) {
        TypedQuery<ExamResult> theQuery = entityManager.createQuery(
                "SELECT er FROM ExamResult er WHERE er.student.id = :studentId ORDER BY er.exam.date DESC",
                ExamResult.class);
        theQuery.setParameter("studentId", studentId);
        return theQuery.getResultList();
    }

    @Override
    public List<ExamResult> findByExamId(int examId) {
        TypedQuery<ExamResult> theQuery = entityManager.createQuery(
                "SELECT er FROM ExamResult er WHERE er.exam.examId = :examId",
                ExamResult.class);
        theQuery.setParameter("examId", examId);
        return theQuery.getResultList();
    }

    @Override
    public void deleteByExamId(int examId) {
        List<ExamResult> results = findByExamId(examId);
        for (ExamResult result : results) {
            entityManager.remove(result);
        }
    }

    @Override
    public void deleteByStudentId(int studentId) {
        List<ExamResult> results = findByStudentId(studentId);
        for (ExamResult result : results) {
            entityManager.remove(result);
        }
    }
}
