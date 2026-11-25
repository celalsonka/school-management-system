package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.CourseSchedule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class CourseScheduleDaoImpl implements CourseScheduleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CourseSchedule save(CourseSchedule courseSchedule) {
        if (courseSchedule.getId() == 0) {
            entityManager.persist(courseSchedule);  // insert
            return courseSchedule;
        } else {
            return entityManager.merge(courseSchedule);  // update
        }
    }

    @Override
    public Optional<CourseSchedule> findById(int id) {
        CourseSchedule courseSchedule = entityManager.find(CourseSchedule.class, id);
        return Optional.ofNullable(courseSchedule);
    }

    @Override
    public List<CourseSchedule> findAll() {
        TypedQuery<CourseSchedule> query = entityManager.createQuery("SELECT cs FROM CourseSchedule cs", CourseSchedule.class);
        return query.getResultList();
    }

    @Override
    public void delete(int id) {
        CourseSchedule courseSchedule = entityManager.find(CourseSchedule.class, id);
        if (courseSchedule != null) {
            entityManager.remove(courseSchedule);
        }
    }

    @Override
    public List<CourseSchedule> findByClassroomCode(String classroomCode) {
        TypedQuery<CourseSchedule> query = entityManager.createQuery(
                "SELECT cs FROM CourseSchedule cs WHERE cs.classroom.classroomCode = :classroomCode", CourseSchedule.class);
        query.setParameter("classroomCode", classroomCode);
        return query.getResultList();
    }

    @Override
    public List<CourseSchedule> findByExamId(int examId) {
        TypedQuery<CourseSchedule> query = entityManager.createQuery(
                "SELECT cs FROM CourseSchedule cs WHERE cs.exam.examId = :examId", CourseSchedule.class);
        query.setParameter("examId", examId);
        return query.getResultList();
    }

    @Override
    public void clearExamFromCourseSchedules(int examId) {
        List<CourseSchedule> schedules = findByExamId(examId);
        for (CourseSchedule schedule : schedules) {
            schedule.setExam(null);
            entityManager.merge(schedule);
        }
    }
}
