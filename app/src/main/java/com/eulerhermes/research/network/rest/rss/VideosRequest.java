package com.eulerhermes.research.network.rest.rss;

public class VideosRequest extends BaseRssRequest {
    protected String endPoint() {
        return "?username=eulerhermes&channel=vid234";
    }

    public String createCacheKey() {
        return "videos";
    }
}
