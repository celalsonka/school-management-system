package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.CourseScheduleDao;
import com.School_Auto.demo.entity.CourseSchedule;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CourseScheduleServiceImpl implements CourseScheduleService {

    private final CourseScheduleDao courseScheduleDao;

    public CourseScheduleServiceImpl(CourseScheduleDao courseScheduleDao) {
        this.courseScheduleDao = courseScheduleDao;
    }

    @Override
    public CourseSchedule save(CourseSchedule courseSchedule) {
        return courseScheduleDao.save(courseSchedule);
    }

    @Override
    public Optional<CourseSchedule> findById(int id) {
        return courseScheduleDao.findById(id);
    }

    @Override
    public List<CourseSchedule> findAll() {
        return courseScheduleDao.findAll();
    }

    @Override
    public void delete(int id) {
        courseScheduleDao.delete(id);
    }

    @Override
    public List<CourseSchedule> findByClassroomCode(String classroomCode) {
        return courseScheduleDao.findByClassroomCode(classroomCode);
    }

    @Override
    @Transactional
    public List<CourseSchedule> findByExamId(int examId) {
        return courseScheduleDao.findByExamId(examId);
    }

    @Override
    @Transactional
    public void clearExamFromCourseSchedules(int examId) {
        courseScheduleDao.clearExamFromCourseSchedules(examId);
    }
}
