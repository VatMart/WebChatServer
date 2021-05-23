package ru.vatmart.webchatserver.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long room_id;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "rooms")
    private List<User> users;

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "room")
    private List<Message> messages;

    public Room() {

    }

    public Long getRoom_id() {
        return room_id;
    }

    public void setRoom_id(Long room_id) {
        this.room_id = room_id;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
