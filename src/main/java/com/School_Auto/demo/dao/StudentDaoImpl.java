package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class StudentDaoImpl implements StudentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Student save(Student student) {
        Student dbStudent = entityManager.merge(student);
        return dbStudent;
    }

    @Override
    public Optional<Student> findById(Long studentId) {
        Student student = entityManager.find(Student.class, studentId);
        return Optional.ofNullable(student);
    }

    @Override
    public List<Student> findAll() {
        TypedQuery<Student> query = entityManager.createQuery("SELECT s FROM Student s", Student.class);
        return query.getResultList();
    }

    @Override
    public void delete(Long studentId) {
        Student student = entityManager.find(Student.class, studentId);
        if (student != null) {
            entityManager.remove(student);
        }
    }

    @Override
    public Optional<Student> findByStudentNo(int studentNo) {
        TypedQuery<Student> query = entityManager.createQuery(
                "SELECT s FROM Student s WHERE s.studentNo = :studentNo", Student.class);
        query.setParameter("studentNo", studentNo);
        List<Student> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<Student> findByLessonId(int lessonId) {
        TypedQuery<Student> query = entityManager.createQuery(
                "SELECT s FROM Student s WHERE s.lesson.lessonId = :lessonId", Student.class);
        query.setParameter("lessonId", lessonId);
        return query.getResultList();
    }

    @Override
    public List<Student> findByPersonId(int personId) {
        TypedQuery<Student> query = entityManager.createQuery(
                "SELECT s FROM Student s WHERE s.person.id = :personId", Student.class);
        query.setParameter("personId", personId);
        return query.getResultList();
    }
    
    @Override
    public List<Student> findBySchoolId(int schoolId) {
        TypedQuery<Student> query = entityManager.createQuery(
                "SELECT s FROM Student s WHERE s.person.school.schoolId = :schoolId", Student.class);
        query.setParameter("schoolId", schoolId);
        return query.getResultList();
    }
}
