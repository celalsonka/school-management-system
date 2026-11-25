package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.ClassDao;
import com.School_Auto.demo.entity._Class;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClassServiceImpl implements ClassService {

    private final ClassDao classDao;

    public ClassServiceImpl(ClassDao classDao) {
        this.classDao = classDao;
    }

    @Override
    public _Class save(_Class _class) {
        return classDao.save(_class);
    }

    @Override
    public Optional<_Class> findById(int id) {
        return classDao.findById(id);
    }

    @Override
    public List<_Class> findAll() {
        return classDao.findAll();
    }

    @Override
    public void delete(int id) {
        classDao.delete(id);
    }

    @Override
    public List<_Class> findByClassLevel(String classLevel) {
        return classDao.findByClassLevel(classLevel);
    }
}
