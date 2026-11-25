package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.StaffDao;
import com.School_Auto.demo.entity.Staff;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {

    private final StaffDao staffDao;

    public StaffServiceImpl(StaffDao staffDao) {
        this.staffDao = staffDao;
    }

    @Override
    public Staff save(Staff staff) {
        return staffDao.save(staff);
    }

    @Override
    public Optional<Staff> findById(Long id) {
        return staffDao.findById(id);
    }

    @Override
    public List<Staff> findAll() {
        return staffDao.findAll();
    }

    @Override
    public void delete(Long id) {
        staffDao.delete(id);
    }

    @Override
    public Optional<Staff> findByEmployeeNumber(String employeeNumber) {
        return staffDao.findByEmployeeNumber(employeeNumber);
    }
}
