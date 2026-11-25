package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Lesson;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class LessonDaoImpl implements LessonDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Lesson save(Lesson lesson) {
        if (lesson.getLessonId() == null || lesson.getLessonId() == 0) {
            entityManager.persist(lesson);  // insert
            return lesson;
        } else {
            return entityManager.merge(lesson);  // update
        }
    }

    @Override
    public Optional<Lesson> findById(int lessonId) {
        Lesson lesson = entityManager.find(Lesson.class, lessonId);
        return Optional.ofNullable(lesson);
    }

    @Override
    public List<Lesson> findAll() {
        TypedQuery<Lesson> query = entityManager.createQuery("SELECT l FROM Lesson l", Lesson.class);
        return query.getResultList();
    }

    @Override
    public void delete(int lessonId) {
        Lesson lesson = entityManager.find(Lesson.class, lessonId);
        if (lesson != null) {
            entityManager.remove(lesson);
        }
    }

    @Override
    public Optional<Lesson> findByLessonCode(String lessonCode) {
        TypedQuery<Lesson> query = entityManager.createQuery(
                "SELECT l FROM Lesson l WHERE l.lessonCode = :lessonCode", Lesson.class);
        query.setParameter("lessonCode", lessonCode);
        List<Lesson> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}
