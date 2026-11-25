package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Manager;
import java.util.List;
import java.util.Optional;

public interface ManagerDao {

    Manager save(Manager manager);  // create or update

    Optional<Manager> findById(Long id);  // read by PK

    List<Manager> findAll();  // read all

    void delete(Long id);  // delete

    Optional<Manager> findByEmployeeNumber(String employeeNumber);  // custom query
}
