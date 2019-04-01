package com.eulerhermes.research.app;

import androidx.fragment.app.FragmentActivity;
import com.eulerhermes.research.network.rest.rss.RssSpiceService;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;

public class BaseActivity extends FragmentActivity
{
    private SpiceManager rssSpiceManager = new SpiceManager(RssSpiceService.class);
    private SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

    protected void onStart() {
        this.spiceManager.start(this);
        this.rssSpiceManager.start(this);
        super.onStart();
    }

    protected void onStop() {
        this.spiceManager.shouldStop();
        this.rssSpiceManager.shouldStop();
        super.onStop();
    }

    protected SpiceManager getSpiceManager() {
        return this.spiceManager;
    }

    protected SpiceManager getRssSpiceManager() {
        return this.rssSpiceManager;
    }
}
