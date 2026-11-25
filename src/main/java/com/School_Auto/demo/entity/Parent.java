package com.School_Auto.demo.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Parent")
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Parent's own primary key

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;  // Person class foreign key

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ParentStudent> parentStudents = new HashSet<>();  // Relationships with students

    // Constructors
    public Parent() {}

    public Parent(Person person) {
        this.person = person;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<ParentStudent> getParentStudents() {
        return parentStudents;
    }

    public void setParentStudents(Set<ParentStudent> parentStudents) {
        this.parentStudents = parentStudents;
    }

    // Helper method to add student with relationship
    public void addStudent(Student student, String relationshipDegree) {
        ParentStudent ps = new ParentStudent(this, student, relationshipDegree);
        this.parentStudents.add(ps);
        student.getParentStudents().add(ps);
    }

    // Helper method to remove student relationship
    public void removeStudent(Student student) {
        parentStudents.removeIf(ps -> ps.getStudent().equals(student));
        student.getParentStudents().removeIf(ps -> ps.getParent().equals(this));
    }
}
