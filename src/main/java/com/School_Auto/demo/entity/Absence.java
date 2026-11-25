package com.School_Auto.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Absence")
public class Absence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long absenceId;  // zayıf varlığın primary key'i

    @Column(name = "date", nullable = false)
    private LocalDate date;  // Tarih

    @Column(name = "absence_type", nullable = false)
    private String absenceType;  // DevamsızlıkTuru

    @Column(name = "lesson_hour", nullable = false)
    private Integer lessonHour;  // DersSaati

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;  // Student ile 1-m ilişkisi

    // Constructors
    public Absence() {}

    public Absence(LocalDate date, String absenceType, Integer lessonHour, Student student) {
        this.date = date;
        this.absenceType = absenceType;
        this.lessonHour = lessonHour;
        this.student = student;
    }

    // Getters and Setters
    public Long getAbsenceId() {
        return absenceId;
    }

    public void setAbsenceId(Long absenceId) {
        this.absenceId = absenceId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAbsenceType() {
        return absenceType;
    }

    public void setAbsenceType(String absenceType) {
        this.absenceType = absenceType;
    }

    public Integer getLessonHour() {
        return lessonHour;
    }

    public void setLessonHour(Integer lessonHour) {
        this.lessonHour = lessonHour;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
