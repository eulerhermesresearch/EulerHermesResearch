package com.eulerhermes.research.network.rest;

public class DocAndVideosRequest extends BaseDocsRequest {
    private final int page;

    public DocAndVideosRequest(int page) {
        this.page = page;
    }

    protected String endPoint() {
        return "/docsandvideos?page=" + this.page + "&perpage=20";
    }

    public String createCacheKey() {
        return "docsandvideos_" + this.page;
    }
}
