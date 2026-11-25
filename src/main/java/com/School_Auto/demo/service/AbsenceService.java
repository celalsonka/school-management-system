package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.Absence;

import java.util.List;
import java.util.Optional;

public interface AbsenceService {

    Absence save(Absence absence);

    Optional<Absence> findById(Long id);

    List<Absence> findAll();

    void delete(Long id);

    List<Absence> findByStudentId(Long studentId);
}
