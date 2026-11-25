package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.LessonDao;
import com.School_Auto.demo.entity.Lesson;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LessonServiceImpl implements LessonService {

    private final LessonDao lessonDao;

    public LessonServiceImpl(LessonDao lessonDao) {
        this.lessonDao = lessonDao;
    }

    @Override
    public Lesson save(Lesson lesson) {
        return lessonDao.save(lesson);
    }

    @Override
    public Optional<Lesson> findById(int lessonId) {
        return lessonDao.findById(lessonId);
    }

    @Override
    public List<Lesson> findAll() {
        return lessonDao.findAll();
    }

    @Override
    public void delete(int lessonId) {
        lessonDao.delete(lessonId);
    }

    @Override
    public Optional<Lesson> findByLessonCode(String lessonCode) {
        return lessonDao.findByLessonCode(lessonCode);
    }
}
