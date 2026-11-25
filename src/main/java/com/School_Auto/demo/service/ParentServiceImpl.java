package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.ParentDao;
import com.School_Auto.demo.entity.Parent;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParentServiceImpl implements ParentService {

    private final ParentDao parentDao;

    public ParentServiceImpl(ParentDao parentDao) {
        this.parentDao = parentDao;
    }

    @Override
    public Parent save(Parent parent) {
        return parentDao.save(parent);
    }

    @Override
    public Optional<Parent> findById(Long id) {
        return parentDao.findById(id);
    }

    @Override
    public List<Parent> findAll() {
        return parentDao.findAll();
    }

    @Override
    public void delete(Long id) {
        parentDao.delete(id);
    }
}
