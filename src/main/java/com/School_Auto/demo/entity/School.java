package com.School_Auto.demo.entity;

import com.School_Auto.demo.security.Passwords;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "school")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "school_id")
    private int schoolId;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "address")
    private String address;

    @Column(name = "password")
    private String password;

    @Column(name = "school_phone")
    private String schoolPhone;

    // === Relationships ===
    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Classroom> classrooms = new HashSet<>();

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Person> persons = new HashSet<>();

    public School() {
    }

    /** The school admin password is stored as a BCrypt hash (no-op if already hashed). */
    @PrePersist
    @PreUpdate
    private void hashPassword() {
        password = Passwords.hashIfNeeded(password);
    }

    public School(String schoolName, String address, String schoolPhone) {
        this.schoolName = schoolName;
        this.address = address;
        this.schoolPhone = schoolPhone;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSchoolPhone() {
        return schoolPhone;
    }

    public void setSchoolPhone(String schoolPhone) {
        this.schoolPhone = schoolPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(Set<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }
}
