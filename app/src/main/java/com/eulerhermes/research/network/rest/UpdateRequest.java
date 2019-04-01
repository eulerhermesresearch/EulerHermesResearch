package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.model.AuthenticateResult;
import com.eulerhermes.research.model.User;
import com.google.api.client.http.HttpContent;
import org.brickred.socialauth.AuthProvider;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateRequest extends NubyServiceRequest<AuthenticateResult> {
    private final User user;

    public UpdateRequest(User user) {
        super(AuthenticateResult.class);
        this.user = user;
    }

    protected String endPoint() {
        return "/update";
    }

    protected int getRequestType() {
        return 1;
    }

    protected HttpContent getContent() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", this.user.getId());
            json.put("company", this.user.getCompany());
            json.put(AuthProvider.COUNTRY, this.user.getCountry());
            json.put("department", this.user.getDepartment());
            json.put("employeesRange", this.user.getEmployeesRange());
            json.put("forename", this.user.getForeName());
            json.put("hasAcceptedInformation", this.user.getHasAcceptedInformation());
            json.put("isCustomerOfEH", this.user.getIsCustomerOfEH());
            json.put("jobTitle", this.user.getJobTitle());
            json.put("name", this.user.getName());
            json.put("phone", new StringBuilder(String.valueOf(this.user.getPhonePrefix())).append(this.user.getPhone()).toString());
            json.put("salesRange", this.user.getSalesRange());
            json.put("sector", this.user.getSector());
            json.put("username", this.user.getUsername());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentFromString(json.toString());
    }
}
