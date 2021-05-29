package ru.vatmart.webchatserver.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.vatmart.webchatserver.entities.enums.Role;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "login", unique = true, nullable = false)
    private String login;

    @Column(name = "nickname", unique = true, nullable = false)
    private String nickname;

    @Column(name = "password", nullable = false, length = 3000)
    private String password;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sender", orphanRemoval = true)
    private List<Message> messages;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_room", joinColumns =  @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id"))
    private List<Room> rooms;

    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    @Column(name = "registration_date", updatable = false)
    private LocalDateTime registrationDate;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    //@JoinColumn(name = "image_id")
    //@OneToOne(mappedBy = "image_id", orphanRemoval = true)
    //private ImageModel avatar;

    @PrePersist
    protected void onCreate() {
        this.registrationDate = LocalDateTime.now();
    }

    public User() {
    }

    public User(String login, String nickname, String password) {
        this.login = login;
        this.nickname = nickname;
        this.password = password;
    }

    public User(Long id, String login, String nickname, String password, List<GrantedAuthority> authorities, LocalDateTime registrationDate) {
        this.user_id = id;
        this.authorities = authorities;
        this.login = login;
        this.nickname = nickname;
        this.password = password;
    }

    public User(Long user_id, String login, String nickname, String password, Set<Role> roles, LocalDateTime registrationDate) {
        this.user_id = user_id;
        this.login = login;
        this.nickname = nickname;
        this.password = password;
        this.roles = roles;
        this.registrationDate = registrationDate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return user_id;
    }

    public void setId(Long user_id) {
        this.user_id = user_id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRole() {
        return roles;
    }

    public void setRole(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
