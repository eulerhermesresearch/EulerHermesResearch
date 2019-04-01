package com.eulerhermes.research.model;

import com.google.api.client.util.Key;

public class ChangePasswordResult {
    @Key("ChangePasswordResult")
    private boolean success;

    public boolean isSuccess() {
        return this.success;
    }
}
