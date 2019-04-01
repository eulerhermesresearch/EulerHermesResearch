package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.model.TwitterFeeds;

public class TwitterFeedRequest extends NubyServiceRequest<TwitterFeeds> {
    public TwitterFeedRequest() {
        super(TwitterFeeds.class);
    }

    protected String endPoint() {
        return "/twitterfeed";
    }

    public String createCacheKey() {
        return "twitterfeed";
    }
}
