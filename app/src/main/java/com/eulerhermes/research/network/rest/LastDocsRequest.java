package com.eulerhermes.research.network.rest;

public class LastDocsRequest extends BaseDocsRequest {
    protected String endPoint() {
        return "/lastdocs";
    }

    public String createCacheKey() {
        return "lastdocs";
    }
}
