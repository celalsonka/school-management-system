package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.Parent;

import java.util.List;
import java.util.Optional;

public interface ParentService {

    Parent save(Parent parent);

    Optional<Parent> findById(Long id);

    List<Parent> findAll();

    void delete(Long id);
}
