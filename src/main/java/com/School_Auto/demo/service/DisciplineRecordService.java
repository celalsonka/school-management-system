package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.DisciplineRecord;

import java.util.List;
import java.util.Optional;

public interface DisciplineRecordService {

    DisciplineRecord save(DisciplineRecord disciplineRecord);

    Optional<DisciplineRecord> findById(Long id);

    List<DisciplineRecord> findAll();

    void delete(Long id);

    List<DisciplineRecord> findByStudentId(Long studentId);
}
