package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.model.SendFeedbackResult;
import com.google.api.client.http.HttpContent;
import org.json.JSONException;
import org.json.JSONObject;

public class MoreAboutEHRequest extends NubyServiceRequest<SendFeedbackResult> {
    private final String forename;
    private final String message;
    private final String name;
    private final String username;

    public MoreAboutEHRequest(String name, String forename, String username, String message) {
        super(SendFeedbackResult.class);
        this.name = name;
        this.forename = forename;
        this.username = username;
        this.message = message;
    }

    protected String endPoint() {
        return "/sendfeedback";
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
            json.put("message", this.message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentFromString(json.toString());
    }
}
