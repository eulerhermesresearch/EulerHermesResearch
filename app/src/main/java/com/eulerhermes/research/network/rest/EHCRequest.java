package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.BuildConfig;

public abstract class EHCRequest<T> extends BaseRequest<T> {
    private static final String AUTHORIZATION_STRING = BuildConfig.BASIC_AUTH_2;
    private static final String NUBY_SERVICE_URL = BuildConfig.SERVER_2 + "/service.svc";

    protected abstract String endPoint();

    public EHCRequest(Class<T> clazz) {
        super(clazz);
    }

    protected String getURL() {
        return new StringBuilder(NUBY_SERVICE_URL).append(endPoint()).toString();
    }

    protected String getAuthorization() {
        return AUTHORIZATION_STRING;
    }
}
