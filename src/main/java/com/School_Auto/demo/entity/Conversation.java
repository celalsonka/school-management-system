package com.School_Auto.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name="conversation")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column(name="person1Id")
    private int person1Id;

    @Column(name="person2Id")
    private int person2Id;

    // Getter ve Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Conversation(){}
    public Conversation( int person1Id, int person2Id) {
        this.person1Id = person1Id;
        this.person2Id = person2Id;
    }

    public int getPerson1Id() { return person1Id; }
    public void setPerson1Id(int person1Id) { this.person1Id = person1Id; }

    public int getPerson2Id() { return person2Id; }
    public void setPerson2Id(int person2Id) { this.person2Id = person2Id; }

}
