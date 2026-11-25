package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.Lesson;

import java.util.List;
import java.util.Optional;

public interface LessonService {

    Lesson save(Lesson lesson);

    Optional<Lesson> findById(int lessonId);

    List<Lesson> findAll();

    void delete(int lessonId);

    Optional<Lesson> findByLessonCode(String lessonCode);
}
