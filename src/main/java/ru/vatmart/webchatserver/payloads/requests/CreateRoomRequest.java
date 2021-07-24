package ru.vatmart.webchatserver.payloads.requests;

import javax.validation.constraints.NotEmpty;

public class CreateRoomRequest {

    @NotEmpty(message = "room name cannot be empty")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
