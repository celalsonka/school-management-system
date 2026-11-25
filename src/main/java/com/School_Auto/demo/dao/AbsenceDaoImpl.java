package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Absence;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class AbsenceDaoImpl implements AbsenceDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Absence save(Absence absence) {
        if (absence.getAbsenceId() == null) {
            entityManager.persist(absence);  // yeni kayıt
            return absence;
        } else {
            return entityManager.merge(absence);  // update
        }
    }

    @Override
    public Optional<Absence> findById(Long id) {
        Absence absence = entityManager.find(Absence.class, id);
        return Optional.ofNullable(absence);
    }

    @Override
    public List<Absence> findAll() {
        TypedQuery<Absence> query = entityManager.createQuery("SELECT a FROM Absence a", Absence.class);
        return query.getResultList();
    }

    @Override
    public void delete(Long id) {
        Absence absence = entityManager.find(Absence.class, id);
        if (absence != null) {
            entityManager.remove(absence);
        }
    }

    @Override
    public List<Absence> findByStudentId(Long studentId) {
        TypedQuery<Absence> query = entityManager.createQuery(
                "SELECT a FROM Absence a WHERE a.student.id = :studentId", Absence.class);
        query.setParameter("studentId", studentId);
        return query.getResultList();
    }
}
