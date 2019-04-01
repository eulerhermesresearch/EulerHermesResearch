package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.model.AuthenticateResult;
import com.google.api.client.http.HttpContent;

public class LoginRequest extends NubyServiceRequest<AuthenticateResult> {
    private final String password;
    private final String username;

    public LoginRequest(String username, String password) {
        super(AuthenticateResult.class);
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
