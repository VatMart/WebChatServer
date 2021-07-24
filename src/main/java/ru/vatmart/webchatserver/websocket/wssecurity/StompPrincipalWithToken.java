package ru.vatmart.webchatserver.websocket.wssecurity;

import java.security.Principal;

public class StompPrincipalWithToken implements Principal {
    private String token;

    StompPrincipalWithToken(String token) {
        this.token = token;
    }
    @Override
    public String getName() {
        return token;
    }
}
