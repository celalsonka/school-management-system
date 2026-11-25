package com.School_Auto.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "_Class")
public class _Class {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int classId;

    public int getClassId() {
        return classId;
    }
    public void setClassId(int classId) {
        this.classId = classId;
    }

    @Column(name = "ClassName")
    private String className;

    @Column(name = "ClassLevel")
    private String classLevel;


    @ManyToOne
    @JoinColumn(name = "lessonId") // burada lessonId kolonu olmalı
    private Lesson lesson;

    public _Class() {
    }

    public _Class(String classLevel, String className) {
        this.classLevel = classLevel;
        this.className = className;
    }

    public _Class(String className, String classLevel, Lesson lesson) {
        this.className = className;
        this.classLevel = classLevel;
        this.lesson = lesson;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(String classLevel) {
        this.classLevel = classLevel;
    }
}
