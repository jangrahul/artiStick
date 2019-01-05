package com.apnihaveli.artiStick.model;

import java.security.Principal;

public class UserAuth {
    private String userId;

    private String name;

    private String authToken;

    public UserAuth() {
    }

    public UserAuth(String userId, String authToken) {
        this.userId = userId;
        this.authToken = authToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
