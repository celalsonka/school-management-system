package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.DisciplineRecord;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class DisciplineRecordDaoImpl implements DisciplineRecordDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public DisciplineRecord save(DisciplineRecord disciplineRecord) {
        if (disciplineRecord.getDisciplineRecordId() == null) {
            entityManager.persist(disciplineRecord);  // insert
            return disciplineRecord;
        } else {
            return entityManager.merge(disciplineRecord);  // update
        }
    }

    @Override
    public Optional<DisciplineRecord> findById(Long id) {
        DisciplineRecord record = entityManager.find(DisciplineRecord.class, id);
        return Optional.ofNullable(record);
    }

    @Override
    public List<DisciplineRecord> findAll() {
        TypedQuery<DisciplineRecord> query = entityManager.createQuery("SELECT d FROM DisciplineRecord d", DisciplineRecord.class);
        return query.getResultList();
    }

    @Override
    public void delete(Long id) {
        DisciplineRecord record = entityManager.find(DisciplineRecord.class, id);
        if (record != null) {
            entityManager.remove(record);
        }
    }

    @Override
    public List<DisciplineRecord> findByStudentId(Long studentId) {
        TypedQuery<DisciplineRecord> query = entityManager.createQuery(
                "SELECT d FROM DisciplineRecord d WHERE d.student.id = :studentId", DisciplineRecord.class);
        query.setParameter("studentId", studentId);
        return query.getResultList();
    }
}
