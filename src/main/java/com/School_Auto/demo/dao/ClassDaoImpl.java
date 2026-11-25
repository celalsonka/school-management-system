package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity._Class;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ClassDaoImpl implements ClassDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public _Class save(_Class _class) {
        if (_class.getClassId() == 0) {
            entityManager.persist(_class);  // yeni kayıt
            return _class;
        } else {
            return entityManager.merge(_class);  // update
        }
    }

    @Override
    public Optional<_Class> findById(int id) {
        _Class _class = entityManager.find(_Class.class, id);
        return Optional.ofNullable(_class);
    }

    @Override
    public List<_Class> findAll() {
        TypedQuery<_Class> query = entityManager.createQuery("SELECT c FROM _Class c", _Class.class);
        return query.getResultList();
    }

    @Override
    public void delete(int id) {
        _Class _class = entityManager.find(_Class.class, id);
        if (_class != null) {
            entityManager.remove(_class);
        }
    }

    @Override
    public List<_Class> findByClassLevel(String classLevel) {
        TypedQuery<_Class> query = entityManager.createQuery(
                "SELECT c FROM _Class c WHERE c.classLevel = :level", _Class.class);
        query.setParameter("level", classLevel);
        return query.getResultList();
    }
}
