package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.model.ChangePasswordResult;
import com.google.api.client.http.HttpContent;

public class ChangePasswordRequest extends NubyServiceRequest<ChangePasswordResult> {
    private final String newPassword;
    private final String oldPassword;
    private final String username;

    public ChangePasswordRequest(String username, String oldPassword, String newPassword) {
        super(ChangePasswordResult.class);
        this.username = username;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    protected String endPoint() {
        return "/changepassword";
    }

    protected int getRequestType() {
        return 1;
    }

    protected HttpContent getContent() {
        return contentFromString("{\"username\":\"" + this.username + "\", \"oldPassword\":\"" + this.oldPassword + "\", \"newPassword\":\"" + this.newPassword + "\"}");
    }
}
