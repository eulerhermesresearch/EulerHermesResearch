package com.eulerhermes.research.network.rest;

import android.util.Log;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.model.AuthenticateResult;
import com.google.api.client.http.HttpContent;

public class KnowingEachOtherBetterRequest extends NubyServiceRequest<AuthenticateResult> {
    private final String employeesRange;
    private final String phone;
    private final String phonePrefix;
    private final String salesRange;

    public KnowingEachOtherBetterRequest(String phonePrefix, String phone, String employeesRange, String salesRange) {
        super(AuthenticateResult.class);
        this.phonePrefix = phonePrefix;
        this.phone = phone;
        this.employeesRange = employeesRange;
        this.salesRange = salesRange;
    }

    protected String endPoint() {
        return "/update";
    }

    protected int getRequestType() {
        return 1;
    }

    protected HttpContent getContent() {
        String json = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("{" + "\"id\":\"" + CorePrefs.getUser().getId() + "\",")).append("\"phone\":\"").append(this.phonePrefix).append(this.phone).append("\",").toString())).append("\"employeesRange\":\"").append(this.employeesRange).append("\",").toString())).append("\"salesRange\":\"").append(this.salesRange).append("\"").toString())).append("}").toString();
        Log.d("KnowingEachOtherBetterRequest", "getContent: " + json);
        return contentFromString(json);
    }
}
