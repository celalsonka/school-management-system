package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.StudentDao;
import com.School_Auto.demo.entity.Student;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentDao studentDao;

    public StudentServiceImpl(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public Student save(Student student) {
        return studentDao.save(student);
    }

    @Override
    public Optional<Student> findById(Long studentId) {
        return studentDao.findById(studentId);
    }

    @Override
    public List<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
    public void delete(Long studentId) {
        studentDao.delete(studentId);
    }

    @Override
    public Optional<Student> findByStudentNo(int studentNo) {
        return studentDao.findByStudentNo(studentNo);
    }

    @Override
    public Student update(Student student) {
        return studentDao.save(student);
    }

    @Override
    public List<Student> findByLessonId(int lessonId) {
        return studentDao.findByLessonId(lessonId);
    }

    @Override
    public List<Student> findByPersonId(int personId) {
        return studentDao.findByPersonId(personId);
    }
    
    @Override
    public List<Student> findBySchoolId(int schoolId) {
        return studentDao.findBySchoolId(schoolId);
    }
}
