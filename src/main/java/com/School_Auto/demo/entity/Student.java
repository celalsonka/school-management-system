package com.School_Auto.demo.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @Column(name = "student_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Parent's own primary key

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ParentStudent> parentStudents = new HashSet<>();  // Relationships with parents

    public Set<ParentStudent> getParentStudents() {
        return parentStudents;
    }

    public void setParentStudents(Set<ParentStudent> parentStudents) {
        this.parentStudents = parentStudents;
    }

    public Set<DisciplineRecord> getDisciplineRecords() {
        return disciplineRecords;
    }

    public void setDisciplineRecords(Set<DisciplineRecord> disciplineRecords) {
        this.disciplineRecords = disciplineRecords;
    }

    public Set<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(Set<Absence> absences) {
        this.absences = absences;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "student_no", nullable = false)
    private int studentNo;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DisciplineRecord> disciplineRecords = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Absence> absences = new HashSet<>();


    // === Foreign Key: Person ===
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    // === Foreign Key: Classroom ===
    @ManyToOne
    @JoinColumn(name = "classroom_code", nullable = true)
    private Classroom classroom;


    public Student() {}

    public Student(int studentNo, Person person) {
        this.studentNo = studentNo;
        this.person = person;
    }



    public int getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(int studentNo) {
        this.studentNo = studentNo;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    @Override
    public String toString() {
        return "Student{" +

                ", studentNo=" + studentNo +
                ", person=" + (person != null ? person.getFirstName() + " " + person.getLastName() : "null") +
                '}';
    }
}
