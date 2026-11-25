package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.Manager;

import java.util.List;
import java.util.Optional;

public interface ManagerService {

    Manager save(Manager manager);

    Optional<Manager> findById(Long id);

    List<Manager> findAll();

    void delete(Long id);

    Optional<Manager> findByEmployeeNumber(String employeeNumber);
}
