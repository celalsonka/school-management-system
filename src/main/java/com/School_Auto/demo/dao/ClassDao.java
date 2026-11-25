package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity._Class;
import java.util.List;
import java.util.Optional;

public interface ClassDao {

    _Class save(_Class _class);  // create or update

    Optional<_Class> findById(int id);  // read by id

    List<_Class> findAll();  // read all

    void delete(int id);  // delete

    List<_Class> findByClassLevel(String classLevel);  // custom query
}
