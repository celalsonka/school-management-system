package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.SchoolDao;
import com.School_Auto.demo.entity.School;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolDao schoolDao;

    public SchoolServiceImpl(SchoolDao schoolDao) {
        this.schoolDao = schoolDao;
    }

    @Override
    public School save(School school) {
        return schoolDao.save(school);
    }

    @Override
    public Optional<School> findById(int schoolId) {
        return schoolDao.findById(schoolId);
    }

    @Override
    public List<School> findAll() {
        return schoolDao.findAll();
    }

    @Override
    public void delete(int schoolId) {
        schoolDao.delete(schoolId);
    }
}
