package com.eulerhermes.research.model;

import com.google.api.client.util.Key;

public class InitPasswordResult {
    @Key("InitPasswordResult")
    private boolean success;

    public boolean isSuccess() {
        return this.success;
    }
}
