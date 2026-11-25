package com.School_Auto.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Manager")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Manager'ın kendi primary key'i

    @Column(name = "sicil_no", nullable = false, unique = true)
    private String employeeNumber;  // sicilNo

    @Column(name = "ise_giris_tarihi", nullable = false)
    private LocalDate hireDate;  // iseGirisTarihi

    @Column(name = "mail", nullable = false, unique = true)
    private String email;  // Mail

    @Column(name = "tel_no")
    private String phoneNumber;  // telNo

    @Column(name = "unvan")
    private String title;  // Unvan

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;

    // Constructors
    public Manager() {}

    public Manager(String employeeNumber, LocalDate hireDate, String email, String phoneNumber, String title, Person person) {
        this.employeeNumber = employeeNumber;
        this.hireDate = hireDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.title = title;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
