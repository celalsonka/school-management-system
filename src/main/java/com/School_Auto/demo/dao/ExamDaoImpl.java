package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Exam;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ExamDaoImpl implements ExamDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Exam save(Exam exam) {
        if (exam.getExamId() == 0) {
            entityManager.persist(exam);  // insert
            return exam;
        } else {
            return entityManager.merge(exam);  // update
        }
    }

    @Override
    public Optional<Exam> findById(int examId) {
        Exam exam = entityManager.find(Exam.class, examId);
        return Optional.ofNullable(exam);
    }

    @Override
    public List<Exam> findAll() {
        TypedQuery<Exam> query = entityManager.createQuery("SELECT e FROM Exam e", Exam.class);
        return query.getResultList();
    }

    @Override
    public void delete(int examId) {
        Exam exam = entityManager.find(Exam.class, examId);
        if (exam != null) {
            entityManager.remove(exam);
        }
    }

    @Override
    public List<Exam> findByLessonId(int lessonId) {
        TypedQuery<Exam> query = entityManager.createQuery(
                "SELECT e FROM Exam e WHERE e.lesson.lessonId = :lessonId", Exam.class);
        query.setParameter("lessonId", lessonId);
        return query.getResultList();
    }
}
