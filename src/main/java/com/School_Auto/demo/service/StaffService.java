package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.Staff;

import java.util.List;
import java.util.Optional;

public interface StaffService {

    Staff save(Staff staff);

    Optional<Staff> findById(Long id);

    List<Staff> findAll();

    void delete(Long id);

    Optional<Staff> findByEmployeeNumber(String employeeNumber);
}
