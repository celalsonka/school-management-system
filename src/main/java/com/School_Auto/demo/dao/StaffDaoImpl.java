package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Staff;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class StaffDaoImpl implements StaffDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Staff save(Staff staff) {
        if (staff.getId() == null) {
            entityManager.persist(staff);  // insert
            return staff;
        } else {
            return entityManager.merge(staff);  // update
        }
    }

    @Override
    public Optional<Staff> findById(Long id) {
        Staff staff = entityManager.find(Staff.class, id);
        return Optional.ofNullable(staff);
    }

    @Override
    public List<Staff> findAll() {
        TypedQuery<Staff> query = entityManager.createQuery("SELECT s FROM Staff s", Staff.class);
        return query.getResultList();
    }

    @Override
    public void delete(Long id) {
        Staff staff = entityManager.find(Staff.class, id);
        if (staff != null) {
            entityManager.remove(staff);
        }
    }

    @Override
    public Optional<Staff> findByEmployeeNumber(String employeeNumber) {
        TypedQuery<Staff> query = entityManager.createQuery(
                "SELECT s FROM Staff s WHERE s.employeeNumber = :employeeNumber", Staff.class);
        query.setParameter("employeeNumber", employeeNumber);
        List<Staff> result = query.getResultList();
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }
}
