package com.School_Auto.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "exam")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private int examId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "hour")
    private String hour;

    @Column(name = "day_of_week")
    private String dayOfWeek;

    @Column(name = "exam_type")
    private String examType;

    // === Foreign Key: Lesson ===
    @ManyToOne
    @JoinColumn(name = "lesson_id", referencedColumnName = "lesson_id")
    private Lesson lesson;

    // === Classroom 1:1 relationship ===
    @OneToOne(mappedBy = "exam")
    private Classroom classroom;

    public Exam() {}

    public Exam(LocalDate date, String hour, String examType, Lesson lesson) {
        this.date = date;
        this.hour = hour;
        this.examType = examType;
        this.lesson = lesson;
    }

    public int getExamId() {
        return examId;
    }

    public void setExamId(int examId) {
        this.examId = examId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "examId=" + examId +
                ", date=" + date +
                ", hour='" + hour + '\'' +
                ", examType='" + examType + '\'' +
                ", lesson=" + (lesson != null ? lesson.getLessonName() : "null") +
                '}';
    }
}
