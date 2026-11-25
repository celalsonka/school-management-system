package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Manager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ManagerDaoImpl implements ManagerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Manager save(Manager manager) {
        if (manager.getId() == null) {
            entityManager.persist(manager);  // insert
            return manager;
        } else {
            return entityManager.merge(manager);  // update
        }
    }

    @Override
    public Optional<Manager> findById(Long id) {
        Manager manager = entityManager.find(Manager.class, id);
        return Optional.ofNullable(manager);
    }

    @Override
    public List<Manager> findAll() {
        TypedQuery<Manager> query = entityManager.createQuery("SELECT m FROM Manager m", Manager.class);
        return query.getResultList();
    }

    @Override
    public void delete(Long id) {
        Manager manager = entityManager.find(Manager.class, id);
        if (manager != null) {
            entityManager.remove(manager);
        }
    }

    @Override
    public Optional<Manager> findByEmployeeNumber(String employeeNumber) {
        TypedQuery<Manager> query = entityManager.createQuery(
                "SELECT m FROM Manager m WHERE m.employeeNumber = :employeeNumber", Manager.class);
        query.setParameter("employeeNumber", employeeNumber);
        List<Manager> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}
