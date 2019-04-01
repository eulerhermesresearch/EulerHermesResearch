package com.eulerhermes.research.model;

import com.google.api.client.util.Key;
import java.util.ArrayList;

public class TwitterFeeds {
    @Key("GetTwitterFeedResult")
    private ArrayList<String> feeds;

    public ArrayList<String> getFeeds() {
        return this.feeds;
    }

    public void setFeeds(ArrayList<String> feeds) {
        this.feeds = feeds;
    }
}
