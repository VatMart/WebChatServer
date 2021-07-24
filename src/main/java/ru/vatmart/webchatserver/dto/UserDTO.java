package ru.vatmart.webchatserver.dto;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

public class UserDTO {
    private Long id;
    @NotEmpty
    private String nickname;
    @NotEmpty
    private LocalDateTime registrationDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
}
