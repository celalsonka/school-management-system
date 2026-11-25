package com.School_Auto.demo.dao;

import com.School_Auto.demo.entity.ParentStudent;
import java.util.List;

public interface ParentStudentDao {
    List<ParentStudent> findAll();
    ParentStudent findById(Long id);
    void save(ParentStudent parentStudent);
    void deleteById(Long id);
    List<ParentStudent> findByParentId(Long parentId);
    List<ParentStudent> findByStudentId(Long studentId);
    void deleteByParentIdAndStudentId(Long parentId, Long studentId);
}
