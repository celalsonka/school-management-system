package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.DisciplineRecordDao;
import com.School_Auto.demo.entity.DisciplineRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DisciplineRecordServiceImpl implements DisciplineRecordService {

    private final DisciplineRecordDao disciplineRecordDao;

    public DisciplineRecordServiceImpl(DisciplineRecordDao disciplineRecordDao) {
        this.disciplineRecordDao = disciplineRecordDao;
    }

    @Override
    public DisciplineRecord save(DisciplineRecord disciplineRecord) {
        return disciplineRecordDao.save(disciplineRecord);
    }

    @Override
    public Optional<DisciplineRecord> findById(Long id) {
        return disciplineRecordDao.findById(id);
    }

    @Override
    public List<DisciplineRecord> findAll() {
        return disciplineRecordDao.findAll();
    }

    @Override
    public void delete(Long id) {
        disciplineRecordDao.delete(id);
    }

    @Override
    public List<DisciplineRecord> findByStudentId(Long studentId) {
        return disciplineRecordDao.findByStudentId(studentId);
    }
}
