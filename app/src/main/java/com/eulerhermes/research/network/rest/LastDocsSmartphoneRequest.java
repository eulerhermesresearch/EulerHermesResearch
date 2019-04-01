package com.eulerhermes.research.network.rest;

public class LastDocsSmartphoneRequest extends BaseDocsRequest {
    protected String endPoint() {
        return "/lastdocsforsmartphone";
    }

    public String createCacheKey() {
        return "lastdocsforsmartphone";
    }
}
