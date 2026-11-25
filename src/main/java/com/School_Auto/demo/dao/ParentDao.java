package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Parent;
import java.util.List;
import java.util.Optional;

public interface ParentDao {

    Parent save(Parent parent);  // create or update

    Optional<Parent> findById(Long id);  // read by PK

    List<Parent> findAll();  // read all

    void delete(Long id);  // delete
}
