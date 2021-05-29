package ru.vatmart.webchatserver.payloads.responses;

import ru.vatmart.webchatserver.entities.enums.Role;

import java.util.Set;

public class JwtResponse {

    private Long user_id;
    private String accToken;
    private long accExpiredInTime;
    private Set<String> roles;

    public JwtResponse(Long user_id, String accToken, long accExpiredInTime, Set<String> roles) {
        this.user_id = user_id;
        this.accToken = accToken;
        this.accExpiredInTime = accExpiredInTime;
        this.roles = roles;
    }

    public JwtResponse() {
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getAccToken() {
        return accToken;
    }

    public void setAccToken(String accToken) {
        this.accToken = accToken;
    }

    public long getAccExpiredInTime() {
        return accExpiredInTime;
    }

    public void setAccExpiredInTime(long accExpiredInTime) {
        this.accExpiredInTime = accExpiredInTime;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
