package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.BuildConfig;

public abstract class NubyServiceRequest<T> extends BaseRequest<T> {
    private static final String NUBY_SERVICE_URL = BuildConfig.SERVER_1 + "/Service.svc";

    protected abstract String endPoint();

    public NubyServiceRequest(Class<T> clazz) {
        super(clazz);
    }

    protected String getURL() {
        return new StringBuilder(NUBY_SERVICE_URL).append(endPoint()).toString();
    }
}
