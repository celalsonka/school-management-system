package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.CourseSchedule;
import java.util.List;
import java.util.Optional;

public interface CourseScheduleDao {

    CourseSchedule save(CourseSchedule courseSchedule);  // create or update

    Optional<CourseSchedule> findById(int id);  // read by PK

    List<CourseSchedule> findAll();  // read all

    void delete(int id);  // delete

    List<CourseSchedule> findByClassroomCode(String classroomCode);  // custom query by classroomCode

    List<CourseSchedule> findByExamId(int examId);  // find by exam id

    void clearExamFromCourseSchedules(int examId);  // clear exam reference from course schedules
}
