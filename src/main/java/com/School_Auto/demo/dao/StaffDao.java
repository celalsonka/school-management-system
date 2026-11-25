package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Staff;
import java.util.List;
import java.util.Optional;

public interface StaffDao {

    Staff save(Staff staff);  // create or update

    Optional<Staff> findById(Long id);  // read by PK

    List<Staff> findAll();  // read all

    void delete(Long id);  // delete

    Optional<Staff> findByEmployeeNumber(String employeeNumber);  // custom query
}
