package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Parent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ParentDaoImpl implements ParentDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Parent save(Parent parent) {
        if (parent.getId() == null) {
            entityManager.persist(parent);  // insert
            return parent;
        } else {
            return entityManager.merge(parent);  // update
        }
    }

    @Override
    public Optional<Parent> findById(Long id) {
        Parent parent = entityManager.find(Parent.class, id);
        return Optional.ofNullable(parent);
    }

    @Override
    public List<Parent> findAll() {
        TypedQuery<Parent> query = entityManager.createQuery("SELECT p FROM Parent p", Parent.class);
        return query.getResultList();
    }

    @Override
    public void delete(Long id) {
        Parent parent = entityManager.find(Parent.class, id);
        if (parent != null) {
            entityManager.remove(parent);
        }
    }
}
