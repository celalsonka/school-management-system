package com.School_Auto.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "parent_student")
public class ParentStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "relationship_degree", nullable = false)
    private String relationshipDegree;  // e.g., "Mother", "Father", "Guardian", "Aunt", "Uncle", etc.

    // Constructors
    public ParentStudent() {}

    public ParentStudent(Parent parent, Student student, String relationshipDegree) {
        this.parent = parent;
        this.student = student;
        this.relationshipDegree = relationshipDegree;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getRelationshipDegree() {
        return relationshipDegree;
    }

    public void setRelationshipDegree(String relationshipDegree) {
        this.relationshipDegree = relationshipDegree;
    }
}
