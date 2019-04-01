package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.model.Docs;

public class BaseDocsRequest extends NubyServiceRequest<Docs> {
    public BaseDocsRequest() {
        super(Docs.class);
    }

    protected String endPoint() {
        return "/lastdocs";
    }
}
