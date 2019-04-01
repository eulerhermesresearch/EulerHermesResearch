package com.eulerhermes.research.network.rest.rss;

public class OurTeamRequest extends BaseRssRequest {
    protected String endPoint() {
        return "?username=eulerhermes&channel=con30";
    }

    public String createCacheKey() {
        return "ourteam";
    }
}
