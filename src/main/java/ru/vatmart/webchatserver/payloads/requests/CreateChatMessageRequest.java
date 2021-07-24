package ru.vatmart.webchatserver.payloads.requests;

import javax.validation.constraints.NotEmpty;

public class CreateChatMessageRequest {
    @NotEmpty
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
