package com.School_Auto.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "SupportStaff")
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Staff'ın kendi primary key'i

    @Column(name = "sicil_no", nullable = false, unique = true)
    private String employeeNumber;  // sicilNo

    @Column(name = "duty")
    private String duty;

    @Column(name = "ise_giris_tarihi", nullable = false)
    private LocalDate hireDate;  // iseGirisTarihi

    @Column(name = "mail", nullable = false, unique = true)
    private String email;  // Mail

    @Column(name = "tel_no")
    private String phoneNumber;  // telNo

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id")
    private Person person;


    // Constructors
    public Staff() {}

    public Staff(String employeeNumber, LocalDate hireDate, String email, String phoneNumber, Person person , String duty) {
        this.employeeNumber = employeeNumber;
        this.hireDate = hireDate;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.person = person;
        this.duty = duty;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
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
}
