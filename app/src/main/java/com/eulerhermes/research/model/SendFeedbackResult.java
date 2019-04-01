package com.eulerhermes.research.model;

import com.google.api.client.util.Key;

public class SendFeedbackResult {
    @Key("SendFeedbackResult")
    private boolean success;

    public boolean isSuccess() {
        return this.success;
    }
}
