package ru.vatmart.webchatserver.entities;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;

    //TODO TEST IT
    @ManyToOne
    private User creator;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "rooms",cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<User> users = new HashSet<>();

    @OneToMany(cascade = {CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "room")
    private List<Message> messages = new ArrayList<>();

    @Column(length = 60)
    private String room_name;

    public Room() {

    }

    public void addMessage(Message message) {
        this.messages.add(message);
        message.getSender().getMessages().add(message);
        message.setRoom(this);
    }

    public void removeMessage(Message message) {
        this.messages.remove(message);
        message.getSender().getMessages().remove(message);
        message.setRoom(null);
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long room_id) {
        this.roomId = room_id;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return roomId.equals(room.roomId) &&
                Objects.equals(creator, room.creator) &&
                Objects.equals(room_name, room.room_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomId, creator, room_name);
    }

    @Override
    public String toString() {
        return "Room{" +
                "room_id=" + roomId +
                ", creator=" + creator +
                ", room_name='" + room_name + '\'' +
                '}';
    }
}
