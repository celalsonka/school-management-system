package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.DisciplineRecord;
import java.util.List;
import java.util.Optional;

public interface DisciplineRecordDao {

    DisciplineRecord save(DisciplineRecord disciplineRecord);  // create or update

    Optional<DisciplineRecord> findById(Long id);  // read by PK

    List<DisciplineRecord> findAll();  // read all

    void delete(Long id);  // delete

    List<DisciplineRecord> findByStudentId(Long studentId);  // custom query by student
}
