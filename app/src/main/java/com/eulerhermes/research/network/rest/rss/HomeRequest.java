package com.eulerhermes.research.network.rest.rss;

public class HomeRequest extends BaseRssRequest {
    protected String endPoint() {
        return "?username=eulerhermes&channel=hom765";
    }

    public String createCacheKey() {
        return "home";
    }
}
