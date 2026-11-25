package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.CourseSchedule;

import java.util.List;
import java.util.Optional;

public interface CourseScheduleService {

    CourseSchedule save(CourseSchedule courseSchedule);

    Optional<CourseSchedule> findById(int id);

    List<CourseSchedule> findAll();

    void delete(int id);

    List<CourseSchedule> findByClassroomCode(String classroomCode);

    List<CourseSchedule> findByExamId(int examId);

    void clearExamFromCourseSchedules(int examId);
}
