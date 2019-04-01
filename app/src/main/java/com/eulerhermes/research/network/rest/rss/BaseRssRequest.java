package com.eulerhermes.research.network.rest.rss;

import com.eulerhermes.research.BuildConfig;
import com.eulerhermes.research.network.EHRetryPolicy;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Channel;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

public abstract class BaseRssRequest extends SpringAndroidSpiceRequest<Channel> {
    private static final String BASE_URL = BuildConfig.SERVER_OLD + "/Handlers/RSS.ashx";

    protected abstract String endPoint();

    public BaseRssRequest() {
        super(Channel.class);
        setRetryPolicy(new EHRetryPolicy());
    }

    public Channel loadDataFromNetwork() throws Exception {
        return (Channel) getRestTemplate().getForObject(new StringBuilder(BASE_URL).append(endPoint()).toString(), Channel.class, new Object[0]);
    }
}
