package com.School_Auto.demo.service;

import com.School_Auto.demo.dao.ClassroomDao;
import com.School_Auto.demo.entity.Classroom;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ClassroomServiceImpl implements ClassroomService {

    private final ClassroomDao classroomDao;

    public ClassroomServiceImpl(ClassroomDao classroomDao) {
        this.classroomDao = classroomDao;
    }

    @Override
    public Classroom save(Classroom classroom) {
        return classroomDao.save(classroom);
    }

    @Override
    public Optional<Classroom> findById(String classroomCode) {
        return classroomDao.findById(classroomCode);
    }

    @Override
    public List<Classroom> findAll() {
        return classroomDao.findAll();
    }

    @Override
    public void delete(String classroomCode) {
        classroomDao.delete(classroomCode);
    }

    @Override
    public List<Classroom> findByType(String type) {
        return classroomDao.findByType(type);
    }
    
    @Override
    public Map<String, Double> getClassroomAverages() {
        Map<String, Double> averages = new HashMap<>();
        List<Object[]> results = classroomDao.getClassroomAverages();
        
        for (Object[] row : results) {
            String classroomCode = (String) row[0];
            // Handle both BigDecimal and numeric types
            Double averageScore = 0.0;
            if (row[1] != null) {
                if (row[1] instanceof BigDecimal) {
                    averageScore = ((BigDecimal) row[1]).doubleValue();
                } else if (row[1] instanceof Number) {
                    averageScore = ((Number) row[1]).doubleValue();
                }
            }
            averages.put(classroomCode, averageScore);
        }
        
        return averages;
    }
}
