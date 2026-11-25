package com.School_Auto.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "DisciplineRecord")
public class DisciplineRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long disciplineRecordId;  // zayıf varlığın primary key'i

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;  // kayıtTarihi

    @Column(name = "reason", nullable = false)
    private String reason;  // sebep

    @Column(name = "result", nullable = false)
    private String result;  // sonuç

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false , referencedColumnName = "student_id")
    private Student student;  // Student ile 1-m ilişkisi

    // Constructors
    public DisciplineRecord() {}

    public DisciplineRecord(LocalDate recordDate, String reason, String result, Student student) {
        this.recordDate = recordDate;
        this.reason = reason;
        this.result = result;
        this.student = student;
    }

    // Getters and Setters
    public Long getDisciplineRecordId() {
        return disciplineRecordId;
    }

    public void setDisciplineRecordId(Long disciplineRecordId) {
        this.disciplineRecordId = disciplineRecordId;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
