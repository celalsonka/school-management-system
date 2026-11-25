package com.School_Auto.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column(name="content")
    private String content;

    @Column(name="timestamp")
    private LocalDateTime timestamp;

    @Column(name="sender_name")
    private String senderName;

    @ManyToOne
    @JoinColumn(name="conversation_id")
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;


    public Message(){}

    // Getter ve Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Message(String content, LocalDateTime timestamp, String senderName, Conversation conversation) {
        this.content = content;
        this.timestamp = timestamp;
        this.senderName = senderName;
        this.conversation = conversation;
    }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public Conversation getConversation() { return conversation; }
    public void setConversation(Conversation conversation) { this.conversation = conversation; }

    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }
}
