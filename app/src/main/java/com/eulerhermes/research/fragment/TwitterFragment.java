package com.eulerhermes.research.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.eulerhermes.research.R;
import com.eulerhermes.research.app.WebActivity;
import com.eulerhermes.research.model.TwitterFeeds;
import com.eulerhermes.research.network.rest.TwitterFeedRequest;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class TwitterFragment extends Fragment
{
    private String lastRequestCacheKey;
    private final SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);
    private TextView text;

    public final class TwitterListener implements RequestListener<TwitterFeeds> {
        public void onRequestFailure(SpiceException spiceException) {
        }

        public void onRequestSuccess(TwitterFeeds feed) {
            if (feed.getFeeds().size() > 0) {
                TwitterFragment.this.text.setText((CharSequence) feed.getFeeds().get(0));
            }
        }
    }

    public static TwitterFragment newInstance() {
        return new TwitterFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_twitter, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.text = (TextView) view.findViewById(R.id.text);
        this.text.setSelected(true);
        this.text.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TwitterFragment.this.openWebURL();
            }
        });
        performRequest();
    }

    public void onStart() {
        this.spiceManager.start(getActivity());
        super.onStart();
    }

    public void onStop() {
        this.spiceManager.shouldStop();
        super.onStop();
    }

    protected SpiceManager getSpiceManager() {
        return this.spiceManager;
    }

    private void performRequest() {
        TwitterFeedRequest request = new TwitterFeedRequest();
        this.lastRequestCacheKey = request.createCacheKey();
        this.spiceManager.getFromCacheAndLoadFromNetworkIfExpired(request, this.lastRequestCacheKey, 18000000, new TwitterListener());
    }

    private void openWebURL() {
        if (this.text.getText() != null && (this.text.getText() instanceof SpannableString)) {
            SpannableString ss = (SpannableString) this.text.getText();
            URLSpan[] spans = (URLSpan[]) ss.getSpans(0, ss.length(), URLSpan.class);
            if (spans.length > 0) {
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("url", spans[0].getURL());
                startActivity(intent);
            }
        }
    }
}
