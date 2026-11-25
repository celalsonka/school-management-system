package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.School;

import java.util.List;
import java.util.Optional;

public interface SchoolService {

    School save(School school);

    Optional<School> findById(int schoolId);

    List<School> findAll();

    void delete(int schoolId);
}
