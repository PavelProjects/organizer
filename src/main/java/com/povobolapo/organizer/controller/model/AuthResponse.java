package com.povobolapo.organizer.controller.model;

import java.io.Serializable;

public class AuthResponse implements Serializable {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
