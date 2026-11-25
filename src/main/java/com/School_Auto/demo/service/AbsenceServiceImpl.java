package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.AbsenceDao;
import com.School_Auto.demo.entity.Absence;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AbsenceServiceImpl implements AbsenceService {

    private final AbsenceDao absenceDao;

    public AbsenceServiceImpl(AbsenceDao absenceDao) {
        this.absenceDao = absenceDao;
    }

    @Override
    public Absence save(Absence absence) {
        return absenceDao.save(absence);
    }

    @Override
    public Optional<Absence> findById(Long id) {
        return absenceDao.findById(id);
    }

    @Override
    public List<Absence> findAll() {
        return absenceDao.findAll();
    }

    @Override
    public void delete(Long id) {
        absenceDao.delete(id);
    }

    @Override
    public List<Absence> findByStudentId(Long studentId) {
        return absenceDao.findByStudentId(studentId);
    }
}
