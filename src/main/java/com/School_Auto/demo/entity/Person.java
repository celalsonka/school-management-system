package com.School_Auto.demo.entity;

import com.School_Auto.demo.security.Passwords;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="person")
public class Person {
    // define fields
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="first_name" )
    private String firstName;

    @Column(name = "city")
    private String city;

    @Column(name = "neighborhood")
    private String neighborhood;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "street")
    private String street;

    @Column(name = "apartment")
    private String apartment;




    public String getTcNo() {
        return tcNo;
    }

    public void setTcNo(String tcNo) {
        this.tcNo = tcNo;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Set<Message> getMessages() {
        return messages;
    }

    public void setMessages(Set<Message> messages) {
        this.messages = messages;
    }

    @Column(name="last_name")
    private String lastName;

    @Column(name="password")
    private String password;

    @Column(name="tcNo")
    private String tcNo;

    @Column(name="birthDate")
    private LocalDateTime birthDate;

    // === Relationships ===
    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Message> messages = new HashSet<>();


    public Person(){}

    /** Passwords are never stored in plain text: hash on every insert/update (no-op if already hashed). */
    @PrePersist
    @PreUpdate
    private void hashPassword() {
        password = Passwords.hashIfNeeded(password);
    }

    public Person(String firstName, String lastName, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public Person(String firstName, String city, String neighborhood, String postalCode, String street, String apartment, String lastName, String password, String tcNo, LocalDateTime birthDate) {
        this.firstName = firstName;
        this.city = city;
        this.neighborhood = neighborhood;
        this.postalCode = postalCode;
        this.street = street;
        this.apartment = apartment;
        this.lastName = lastName;
        this.password = password;
        this.tcNo = tcNo;
        this.birthDate = birthDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // define toString
    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
