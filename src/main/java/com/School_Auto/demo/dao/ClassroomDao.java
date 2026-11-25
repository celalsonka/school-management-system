package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.Classroom;
import java.util.List;
import java.util.Optional;

public interface ClassroomDao {

    Classroom save(Classroom classroom);  // create or update

    Optional<Classroom> findById(String classroomCode);  // read by PK

    List<Classroom> findAll();  // read all

    void delete(String classroomCode);  // delete

    List<Classroom> findByType(String type);  // custom query by type
    
    /**
     * Call stored procedure to get classroom averages
     * Returns list of Object[] where each array contains:
     * [0] classroom_code (String), [1] average_score (BigDecimal), 
     * [2] total_students (BigInteger), [3] total_exams (BigInteger)
     */
    List<Object[]> getClassroomAverages();
}
