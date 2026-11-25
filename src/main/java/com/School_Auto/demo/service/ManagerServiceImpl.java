package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.ManagerDao;
import com.School_Auto.demo.entity.Manager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerServiceImpl implements ManagerService {

    private final ManagerDao managerDao;

    public ManagerServiceImpl(ManagerDao managerDao) {
        this.managerDao = managerDao;
    }

    @Override
    public Manager save(Manager manager) {
        return managerDao.save(manager);
    }

    @Override
    public Optional<Manager> findById(Long id) {
        return managerDao.findById(id);
    }

    @Override
    public List<Manager> findAll() {
        return managerDao.findAll();
    }

    @Override
    public void delete(Long id) {
        managerDao.delete(id);
    }

    @Override
    public Optional<Manager> findByEmployeeNumber(String employeeNumber) {
        return managerDao.findByEmployeeNumber(employeeNumber);
    }
}
