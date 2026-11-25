package com.School_Auto.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Teacher")
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Teacher's own primary key

    @Column(name = "sicil_no", nullable = false, unique = true)
    private String employeeNumber;  // sicilNo

    @Column(name = "ise_giris_tarihi", nullable = false)
    private LocalDate hireDate;  // iseGirisTarihi

    @Column(name = "mail", nullable = false, unique = true)
    private String email;  // Mail

    @Column(name = "tel_no")
    private String phoneNumber;  // telNo

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    // === Relationships ===
    @ManyToMany(mappedBy = "teachers")
    private Set<Lesson> lessons = new HashSet<>();


    // Constructors
    public Teacher() {}

    public Teacher(String employeeNumber, LocalDate hireDate, String email, String phoneNumber, Person person) {
        this.employeeNumber = employeeNumber;
        this.hireDate = hireDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.person = person;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(Set<Lesson> lessons) {
        this.lessons = lessons;
    }
}
