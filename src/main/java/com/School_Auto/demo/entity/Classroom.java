package com.School_Auto.demo.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "classroom")
public class Classroom {

    @Id
    @Column(name = "classroom_code")
    private String classroomCode;  // Primary Key

    @Column(name = "capacity")
    private int capacity;

    @Column(name = "type")
    private String type;

    @Column(name = "location")
    private String location;

    // === Relationships ===
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Lesson> lessons = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "exam_id")
    private Exam exam;

    @OneToMany(mappedBy = "classroom")
    private Set<CourseSchedule> courseSchedules = new HashSet<>();

    public Classroom() {
    }

    public Classroom(String classroomCode, int capacity, String type, String location) {
        this.classroomCode = classroomCode;
        this.capacity = capacity;
        this.type = type;
        this.location = location;
    }

    public String getClassroomCode() {
        return classroomCode;
    }

    public void setClassroomCode(String classroomCode) {
        this.classroomCode = classroomCode;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Set<CourseSchedule> getCourseSchedules() {
        return courseSchedules;
    }

    public void setCourseSchedules(Set<CourseSchedule> courseSchedules) {
        this.courseSchedules = courseSchedules;
    }

    @Override
    public String toString() {
        return "Classroom{" +
                "classroomCode='" + classroomCode + '\'' +
                ", capacity=" + capacity +
                ", type='" + type + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
