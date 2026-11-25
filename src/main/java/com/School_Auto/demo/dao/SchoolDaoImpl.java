package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.School;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class SchoolDaoImpl implements SchoolDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public School save(School school) {
        if (school.getSchoolId() == 0) {
            entityManager.persist(school);  // insert
            return school;
        } else {
            return entityManager.merge(school);  // update
        }
    }

    @Override
    public Optional<School> findById(int schoolId) {
        School school = entityManager.find(School.class, schoolId);
        return Optional.ofNullable(school);
    }

    @Override
    public List<School> findAll() {
        TypedQuery<School> query = entityManager.createQuery("SELECT s FROM School s", School.class);
        return query.getResultList();
    }

    @Override
    public void delete(int schoolId) {
        School school = entityManager.find(School.class, schoolId);
        if (school != null) {
            entityManager.remove(school);
        }
    }
}
