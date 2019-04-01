package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.model.InitPasswordResult;
import com.google.api.client.http.HttpContent;
import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordRequest extends NubyServiceRequest<InitPasswordResult> {
    private final String forename;
    private final String name;
    private final String username;

    public ForgotPasswordRequest(String name, String forename, String username) {
        super(InitPasswordResult.class);
        this.name = name;
        this.forename = forename;
        this.username = username;
    }

    protected String endPoint() {
        return "/initpassword";
    }

    protected int getRequestType() {
        return 1;
    }

    protected HttpContent getContent() {
        JSONObject json = new JSONObject();
        try {
            json.put("name", this.name);
            json.put("forename", this.forename);
            json.put("username", this.username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentFromString(json.toString());
    }
}
