package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.TeacherDao;
import com.School_Auto.demo.entity.Teacher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    private final TeacherDao teacherDao;

    public TeacherServiceImpl(TeacherDao teacherDao) {
        this.teacherDao = teacherDao;
    }

    @Override
    public Teacher save(Teacher teacher) {
        return teacherDao.save(teacher);
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        return teacherDao.findById(id);
    }

    @Override
    public List<Teacher> findAll() {
        return teacherDao.findAll();
    }

    @Override
    public void delete(Long id) {
        teacherDao.delete(id);
    }

    @Override
    public Optional<Teacher> findByEmployeeNumber(String employeeNumber) {
        return teacherDao.findByEmployeeNumber(employeeNumber);
    }
}
