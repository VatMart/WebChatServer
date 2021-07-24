package ru.vatmart.webchatserver.entities;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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
    private LocalDateTime sendingDate;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, targetEntity = User.class)
    //@JoinColumn(name = "user_id")
    private User sender;

    @Column(name = "answering_message_id")
    private Long answering_message_id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST},targetEntity = Room.class)
    //@JoinColumn(name = "room_id")
    private Room room;

    @PrePersist
    protected void onCreate() {
        this.sendingDate = LocalDateTime.now();
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

    public LocalDateTime getSendingDate() {
        return sendingDate;
    }

    public void setSendingDate(LocalDateTime sending_date) {
        this.sendingDate = sending_date;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return message_id.equals(message.message_id) &&
                Objects.equals(text, message.text) &&
                Objects.equals(sendingDate, message.sendingDate) &&
                Objects.equals(sender, message.sender) &&
                Objects.equals(answering_message_id, message.answering_message_id) &&
                Objects.equals(room, message.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message_id, text, sendingDate, sender, answering_message_id, room);
    }

    @Override
    public String toString() {
        return "Message{" +
                "message_id=" + message_id +
                ", text='" + text + '\'' +
                ", sending_date=" + sendingDate +
                ", sender=" + sender +
                ", answering_message_id=" + answering_message_id +
                ", room=" + room +
                '}';
    }
}
