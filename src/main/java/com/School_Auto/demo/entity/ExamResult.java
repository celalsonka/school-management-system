package com.School_Auto.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "exam_result")
public class ExamResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private int resultId;

    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "exam_id", referencedColumnName = "exam_id")
    private Exam exam;

    @Column(name = "score")
    private Double score;

    public ExamResult() {}

    public ExamResult(Student student, Exam exam, Double score) {
        this.student = student;
        this.exam = exam;
        this.score = score;
    }

    public int getResultId() {
        return resultId;
    }

    public void setResultId(int resultId) {
        this.resultId = resultId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "ExamResult{" +
                "resultId=" + resultId +
                ", student=" + student +
                ", exam=" + exam +
                ", score=" + score +
                '}';
    }
}
