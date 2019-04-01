package com.eulerhermes.research.network.rest.rss;

public class NewsRequest extends BaseRssRequest {
    protected String endPoint() {
        return "?username=eulerhermes&channel=new140";
    }

    public String createCacheKey() {
        return "news";
    }
}
