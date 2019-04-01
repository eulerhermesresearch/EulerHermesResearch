package com.eulerhermes.research.model;

import com.google.api.client.util.Key;

public class SubscribeResult {
    @Key("SubscribeResult")
    private String id;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
