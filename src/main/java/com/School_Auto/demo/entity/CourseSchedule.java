package com.School_Auto.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "course_schedule")
public class CourseSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "day")
    private String day;

    @Column(name = "lesson_hour")
    private String lessonHour;

    // === Foreign key (Lesson tablosuna) ===
    @ManyToOne
    @JoinColumn(name = "lesson_id", referencedColumnName = "lesson_id")
    private Lesson lesson;

    // === Foreign key (Exam tablosuna) ===
    @ManyToOne
    @JoinColumn(name = "exam_id", referencedColumnName = "exam_id")
    private Exam exam;

    // === Classroom M:1 relationship (many schedules belong to one classroom) ===
    @ManyToOne
    @JoinColumn(name = "classroom_code", referencedColumnName = "classroom_code")
    private Classroom classroom;

    public CourseSchedule() {}

    public CourseSchedule(String day, String lessonHour, Lesson lesson) {
        this.day = day;
        this.lessonHour = lessonHour;
        this.lesson = lesson;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDay() { return day; }
    public void setDay(String day) { this.day = day; }

    public String getLessonHour() { return lessonHour; }
    public void setLessonHour(String lessonHour) { this.lessonHour = lessonHour; }

    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }

    public Exam getExam() { return exam; }
    public void setExam(Exam exam) { this.exam = exam; }

    public Classroom getClassroom() { return classroom; }
    public void setClassroom(Classroom classroom) { this.classroom = classroom; }

    @Override
    public String toString() {
        return "CourseSchedule{" +
                "id=" + id +
                ", day='" + day + '\'' +
                ", lessonHour='" + lessonHour + '\'' +
                ", lesson=" + (lesson != null ? lesson.getLessonName() : "null") +
                ", exam=" + (exam != null ? exam.getExamType() : "null") +
                ", classroom=" + (classroom != null ? classroom.getClassroomCode() : "null") +
                '}';
    }
}
