package ru.vatmart.webchatserver.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long message_id;

    @Column(name = "text", nullable = false, columnDefinition = "text")
    private String text;

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(name = "sending_date", updatable = false)
    private LocalDateTime sending_date;

    @ManyToOne(targetEntity = User.class)
    //@JoinColumn(name = "user_id")
    private User sender;

    @Column(name = "answering_message_id")
    private Long answering_message_id;

    @ManyToOne(targetEntity = Room.class)
    //@JoinColumn(name = "room_id")
    private Room room;

    @PrePersist
    protected void onCreate() {
        this.sending_date = LocalDateTime.now();
    }

    public Message() {

    }

    public Long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(Long message_id) {
        this.message_id = message_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSending_date() {
        return sending_date;
    }

    public void setSending_date(LocalDateTime sending_date) {
        this.sending_date = sending_date;
    }

    public User getSender_id() {
        return sender;
    }

    public void setSender_id(User sender) {
        this.sender = sender;
    }

    public Long getAnswering_message_id() {
        return answering_message_id;
    }

    public void setAnswering_message_id(Long answering_message_id) {
        this.answering_message_id = answering_message_id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
