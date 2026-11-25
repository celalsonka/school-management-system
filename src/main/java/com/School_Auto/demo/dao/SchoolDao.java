package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.School;
import java.util.List;
import java.util.Optional;

public interface SchoolDao {

    School save(School school);  // create or update

    Optional<School> findById(int schoolId);  // read by PK

    List<School> findAll();  // read all

    void delete(int schoolId);  // delete
}
