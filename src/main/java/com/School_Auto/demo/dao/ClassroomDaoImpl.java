package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Classroom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ClassroomDaoImpl implements ClassroomDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Classroom save(Classroom classroom) {
        if (entityManager.find(Classroom.class, classroom.getClassroomCode()) == null) {
            entityManager.persist(classroom);  // insert
            return classroom;
        } else {
            return entityManager.merge(classroom);  // update
        }
    }

    @Override
    public Optional<Classroom> findById(String classroomCode) {
        Classroom classroom = entityManager.find(Classroom.class, classroomCode);
        return Optional.ofNullable(classroom);
    }

    @Override
    public List<Classroom> findAll() {
        TypedQuery<Classroom> query = entityManager.createQuery("SELECT c FROM Classroom c", Classroom.class);
        return query.getResultList();
    }

    @Override
    public void delete(String classroomCode) {
        Classroom classroom = entityManager.find(Classroom.class, classroomCode);
        if (classroom != null) {
            entityManager.remove(classroom);
        }
    }

    @Override
    public List<Classroom> findByType(String type) {
        TypedQuery<Classroom> query = entityManager.createQuery(
                "SELECT c FROM Classroom c WHERE c.type = :type", Classroom.class);
        query.setParameter("type", type);
        return query.getResultList();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> getClassroomAverages() {
        String sql = "SELECT * FROM get_classroom_averages()";
        return entityManager.createNativeQuery(sql).getResultList();
    }
}
