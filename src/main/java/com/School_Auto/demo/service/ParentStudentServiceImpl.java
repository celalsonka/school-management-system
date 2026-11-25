package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.ParentStudentDao;
import com.School_Auto.demo.entity.ParentStudent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParentStudentServiceImpl implements ParentStudentService {

    private ParentStudentDao parentStudentDao;

    public ParentStudentServiceImpl(ParentStudentDao theParentStudentDao) {
        this.parentStudentDao = theParentStudentDao;
    }

    @Override
    public List<ParentStudent> findAll() {
        return parentStudentDao.findAll();
    }

    @Override
    public ParentStudent findById(Long id) {
        return parentStudentDao.findById(id);
    }

    @Override
    @Transactional
    public void save(ParentStudent parentStudent) {
        parentStudentDao.save(parentStudent);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        parentStudentDao.deleteById(id);
    }

    @Override
    public List<ParentStudent> findByParentId(Long parentId) {
        return parentStudentDao.findByParentId(parentId);
    }

    @Override
    public List<ParentStudent> findByStudentId(Long studentId) {
        return parentStudentDao.findByStudentId(studentId);
    }

    @Override
    @Transactional
    public void deleteByParentIdAndStudentId(Long parentId, Long studentId) {
        parentStudentDao.deleteByParentIdAndStudentId(parentId, studentId);
    }
}
