package com.School_Auto.demo.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Integer lessonId;

    @Column(name = "lesson_code", nullable = false, unique = true)
    private String lessonCode;

    @Column(name = "lesson_name", nullable = false)
    private String lessonName;

    // === Relationships ===
    @ManyToMany
    @JoinTable(
            name = "teacher_lesson",
            joinColumns = @JoinColumn(name = "lesson_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "lesson", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Exam> exams = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "classroom_code")
    private Classroom classroom;

    public Lesson() {
    }

    public Lesson(String lessonCode, String lessonName) {
        this.lessonCode = lessonCode;
        this.lessonName = lessonName;
    }


    public Integer getLessonId() {
        return lessonId;
    }

    public void setLessonId(Integer lessonId) {
        this.lessonId = lessonId;
    }

    public String getLessonCode() {
        return lessonCode;
    }

    public void setLessonCode(String lessonCode) {
        this.lessonCode = lessonCode;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Set<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<Teacher> teachers) {
        this.teachers = teachers;
    }

    public Set<Exam> getExams() {
        return exams;
    }

    public void setExams(Set<Exam> exams) {
        this.exams = exams;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "lessonId=" + lessonId +
                ", lessonCode='" + lessonCode + '\'' +
                ", lessonName='" + lessonName + '\'' +
                '}';
    }
}
