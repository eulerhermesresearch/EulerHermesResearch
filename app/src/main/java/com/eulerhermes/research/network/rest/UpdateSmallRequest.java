package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.model.AuthenticateResult;
import com.eulerhermes.research.model.User;
import com.google.api.client.http.HttpContent;
import org.brickred.socialauth.AuthProvider;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateSmallRequest extends NubyServiceRequest<AuthenticateResult> {
    private final String company;
    private final String country;
    private final String department;
    private final boolean isEHCustomer;
    private final String job;
    private final String sector;

    public UpdateSmallRequest(String company, String sector, String job, String department, boolean isEHCustomer, String country) {
        super(AuthenticateResult.class);
        this.company = company;
        this.sector = sector;
        this.job = job;
        this.department = department;
        this.isEHCustomer = isEHCustomer;
        this.country = country;
    }

    protected String endPoint() {
        return "/update";
    }

    protected int getRequestType() {
        return 1;
    }

    protected HttpContent getContent() {
        User user = CorePrefs.getUser();
        JSONObject json = new JSONObject();
        try {
            json.put("id", user.getId());
            json.put("company", this.company);
            json.put(AuthProvider.COUNTRY, this.country);
            json.put("department", this.department);
            json.put("isCustomerOfEH", this.isEHCustomer);
            json.put("jobTitle", this.job);
            json.put("sector", this.sector);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return contentFromString(json.toString());
    }
}
