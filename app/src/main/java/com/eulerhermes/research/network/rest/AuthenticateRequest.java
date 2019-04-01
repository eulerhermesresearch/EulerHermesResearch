package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.model.User;
import com.google.api.client.http.HttpContent;

public class AuthenticateRequest extends NubyServiceRequest<User> {
    private final String password;
    private final String username;

    public AuthenticateRequest(String username, String password) {
        super(User.class);
        this.username = username;
        this.password = password;
    }

    protected String endPoint() {
        return "/authenticate";
    }

    protected int getRequestType() {
        return 1;
    }

    protected HttpContent getContent() {
        return contentFromString("{\"username\":\"" + this.username + "\", \"password\":\"" + this.password + "\"}");
    }
}
