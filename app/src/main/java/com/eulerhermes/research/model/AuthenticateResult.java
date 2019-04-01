package com.eulerhermes.research.model;

import com.google.api.client.util.Key;

public class AuthenticateResult {
    @Key("AuthenticateResult")
    private User user;

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
