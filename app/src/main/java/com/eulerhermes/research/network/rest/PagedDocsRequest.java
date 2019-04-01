package com.eulerhermes.research.network.rest;

public class PagedDocsRequest extends BaseDocsRequest {
    private final int page;

    public PagedDocsRequest(int page) {
        this.page = page;
    }

    protected String endPoint() {
        return "/docs?page=" + this.page + "&perpage=10";
    }

    public String createCacheKey() {
        return "docs";
    }
}
