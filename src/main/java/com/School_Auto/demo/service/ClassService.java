package com.School_Auto.demo.service;

import com.School_Auto.demo.entity._Class;

import java.util.List;
import java.util.Optional;

public interface ClassService {

    _Class save(_Class _class);

    Optional<_Class> findById(int id);

    List<_Class> findAll();

    void delete(int id);

    List<_Class> findByClassLevel(String classLevel);
}
