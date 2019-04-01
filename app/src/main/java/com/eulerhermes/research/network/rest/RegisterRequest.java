package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.model.SubscribeResult;
import com.google.api.client.http.HttpContent;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterRequest extends NubyServiceRequest<SubscribeResult> {
    private final String forename;
    private final Boolean hasAcceptedInformation;
    private final String name;
    private final String username;

    public RegisterRequest(String name, String forename, String username, Boolean hasAcceptedInformation) {
        super(SubscribeResult.class);
        this.name = name;
        this.forename = forename;
        this.username = username;
        this.hasAcceptedInformation = hasAcceptedInformation;
    }

    protected String endPoint() {
        return "/subscribe";
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
            json.put("hasAcceptedInformation", this.hasAcceptedInformation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentFromString(json.toString());
    }
}
