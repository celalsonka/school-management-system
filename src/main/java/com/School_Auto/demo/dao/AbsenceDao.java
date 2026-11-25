package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Absence;
import java.util.List;
import java.util.Optional;

public interface AbsenceDao {

    Absence save(Absence absence);  // create or update

    Optional<Absence> findById(Long id);  // read by id

    List<Absence> findAll();  // read all

    void delete(Long id);  // delete

    List<Absence> findByStudentId(Long studentId);  // custom query
}
