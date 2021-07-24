package ru.vatmart.webchatserver.websocket.wssecurity;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler  extends DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {
        MultiValueMap<String, String> parameters =
                UriComponentsBuilder.fromUriString(request.getURI().toString()).build().getQueryParams();
        System.out.println("custom handshake handler works! tokenValue=" + parameters.get("auth").get(0));
        return new StompPrincipalWithToken(parameters.get("auth").get(0));
    }
}
