package com.eulerhermes.research.network.rest;

public class VideosRequest extends BaseDocsRequest {
    private final int page;

    public VideosRequest(int page) {
        this.page = page;
    }

    protected String endPoint() {
        return "/docsandvideos?type=VIDEO&page=" + this.page + "&perpage=20";
    }

    public String createCacheKey() {
        return "docsandvideos_videos_" + this.page;
    }
}
