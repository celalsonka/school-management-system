package com.School_Auto.demo.service;

import com.School_Auto.demo.entity.Classroom;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClassroomService {

    Classroom save(Classroom classroom);

    Optional<Classroom> findById(String classroomCode);

    List<Classroom> findAll();

    void delete(String classroomCode);

    List<Classroom> findByType(String type);
    
    /**
     * Get classroom averages from stored procedure
     * Returns a map with classroom_code as key and average_score as value
     */
    Map<String, Double> getClassroomAverages();
}
