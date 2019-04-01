package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.Page;
import com.google.api.client.http.HttpContent;

public class SearchRequest extends BaseDocsRequest {
    private final String keyword;

    public SearchRequest(String keyword) {
        this.keyword = keyword;
    }

    protected String endPoint() {
        return "/docs/search";
    }

    public String createCacheKey() {
        return "doc_search_" + this.keyword.replace(" ", Page.SIMPLE_DATA_KEY);
    }

    protected int getRequestType() {
        return 1;
    }

    protected HttpContent getContent() {
        return contentFromString("{\"name\":\"" + this.keyword + "\"}");
    }
}
