package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.fragment.contact.wizardpager.wizard.model.Page;

public class InfographicsRequest extends BaseDocsRequest {
    private final int page;
    private final String type;

    public InfographicsRequest(int page, String type) {
        this.page = page;
        this.type = type;
    }

    protected String endPoint() {
        return "/docs?page=" + this.page + "&perpage=20&type=" + this.type;
    }

    public String createCacheKey() {
        return "docs_" + this.page + Page.SIMPLE_DATA_KEY + this.type;
    }
}
