package com.eulerhermes.research.network;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.retry.RetryPolicy;

public class EHRetryPolicy implements RetryPolicy {
    public static final float DEFAULT_BACKOFF_MULT = 1.0f;
    public static final long DEFAULT_DELAY_BEFORE_RETRY = 500;
    public static final int DEFAULT_RETRY_COUNT = 3;
    private float backOffMultiplier;
    private long delayBeforeRetry;
    private int retryCount;

    public EHRetryPolicy(int retryCount, long delayBeforeRetry, float backOffMultiplier) {
        this.retryCount = 3;
        this.delayBeforeRetry = 500;
        this.backOffMultiplier = 1.0f;
        this.retryCount = retryCount;
        this.delayBeforeRetry = delayBeforeRetry;
        this.backOffMultiplier = backOffMultiplier;
    }

    public EHRetryPolicy() {
        this(3, 500, 1.0f);
    }

    public int getRetryCount() {
        return this.retryCount;
    }

    public void retry(SpiceException e) {
        this.retryCount--;
        this.delayBeforeRetry = (long) (((float) this.delayBeforeRetry) * this.backOffMultiplier);
    }

    public long getDelayBeforeRetry() {
        return this.delayBeforeRetry;
    }
}
