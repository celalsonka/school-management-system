package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Lesson;
import java.util.List;
import java.util.Optional;

public interface LessonDao {

    Lesson save(Lesson lesson);  // create or update

    Optional<Lesson> findById(int lessonId);  // read by PK

    List<Lesson> findAll();  // read all

    void delete(int lessonId);  // delete

    Optional<Lesson> findByLessonCode(String lessonCode);  // custom query
}
