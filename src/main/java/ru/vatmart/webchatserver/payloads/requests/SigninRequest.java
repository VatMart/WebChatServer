package ru.vatmart.webchatserver.payloads.requests;

import javax.validation.constraints.NotEmpty;

public class SigninRequest {
    @NotEmpty(message = "Username cannot be empty")
    private String login;
    @NotEmpty(message = "Password cannot be empty")
    private String password;

    public SigninRequest() {
    }

    public SigninRequest(@NotEmpty(message = "Username cannot be empty") String login, @NotEmpty(message = "Password cannot be empty") String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
