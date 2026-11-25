package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.ParentStudent;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ParentStudentDaoImpl implements ParentStudentDao {

    private EntityManager entityManager;

    public ParentStudentDaoImpl(EntityManager theEntityManager) {
        this.entityManager = theEntityManager;
    }

    @Override
    public List<ParentStudent> findAll() {
        TypedQuery<ParentStudent> theQuery = entityManager.createQuery(
                "from ParentStudent", ParentStudent.class);
        return theQuery.getResultList();
    }

    @Override
    public ParentStudent findById(Long id) {
        return entityManager.find(ParentStudent.class, id);
    }

    @Override
    @Transactional
    public void save(ParentStudent parentStudent) {
        entityManager.persist(parentStudent);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ParentStudent parentStudent = entityManager.find(ParentStudent.class, id);
        if (parentStudent != null) {
            entityManager.remove(parentStudent);
        }
    }

    @Override
    public List<ParentStudent> findByParentId(Long parentId) {
        TypedQuery<ParentStudent> theQuery = entityManager.createQuery(
                "SELECT ps FROM ParentStudent ps WHERE ps.parent.id = :parentId", ParentStudent.class);
        theQuery.setParameter("parentId", parentId);
        return theQuery.getResultList();
    }

    @Override
    public List<ParentStudent> findByStudentId(Long studentId) {
        TypedQuery<ParentStudent> theQuery = entityManager.createQuery(
                "SELECT ps FROM ParentStudent ps WHERE ps.student.id = :studentId", ParentStudent.class);
        theQuery.setParameter("studentId", studentId);
        return theQuery.getResultList();
    }

    @Override
    @Transactional
    public void deleteByParentIdAndStudentId(Long parentId, Long studentId) {
        TypedQuery<ParentStudent> theQuery = entityManager.createQuery(
                "SELECT ps FROM ParentStudent ps WHERE ps.parent.id = :parentId AND ps.student.id = :studentId", 
                ParentStudent.class);
        theQuery.setParameter("parentId", parentId);
        theQuery.setParameter("studentId", studentId);
        
        List<ParentStudent> results = theQuery.getResultList();
        for (ParentStudent ps : results) {
            entityManager.remove(ps);
        }
    }
}
