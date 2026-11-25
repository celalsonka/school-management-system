package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Teacher;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class TeacherDaoImpl implements TeacherDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Teacher save(Teacher teacher) {
        if (teacher.getId() == null) {
            entityManager.persist(teacher);  // insert
            return teacher;
        } else {
            return entityManager.merge(teacher);  // update
        }
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        Teacher teacher = entityManager.find(Teacher.class, id);
        return Optional.ofNullable(teacher);
    }

    @Override
    public List<Teacher> findAll() {
        TypedQuery<Teacher> query = entityManager.createQuery("SELECT t FROM Teacher t", Teacher.class);
        return query.getResultList();
    }

    @Override
    public void delete(Long id) {
        Teacher teacher = entityManager.find(Teacher.class, id);
        if (teacher != null) {
            entityManager.remove(teacher);
        }
    }

    @Override
    public Optional<Teacher> findByEmployeeNumber(String employeeNumber) {
        TypedQuery<Teacher> query = entityManager.createQuery(
                "SELECT t FROM Teacher t WHERE t.employeeNumber = :employeeNumber", Teacher.class);
        query.setParameter("employeeNumber", employeeNumber);
        List<Teacher> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}
