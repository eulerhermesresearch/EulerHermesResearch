package com.eulerhermes.research.network.rest;

public class CategoryDocsRequest extends BaseDocsRequest {
    private final int category;

    public CategoryDocsRequest(int category) {
        this.category = category;
    }

    protected String endPoint() {
        return "/docs/" + this.category;
    }

    public String createCacheKey() {
        return "docs_" + this.category;
    }
}
