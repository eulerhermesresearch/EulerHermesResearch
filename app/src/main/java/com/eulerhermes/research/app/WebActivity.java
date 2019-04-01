/**
 *
 */

package com.eulerhermes.research.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.IntentExtras;
import com.eulerhermes.research.view.EmptyView;

public class WebActivity extends Activity
{
    private WebView				mWebView;
    private EmptyView			mEmptyView;
    private boolean				mError;

    public WebActivity()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //getWindow().requestFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_web);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setLogo(R.drawable.ic_arrow_left);

        mWebView = (WebView) findViewById(R.id.webview);
        mEmptyView = (EmptyView) findViewById(android.R.id.empty);

        final ProgressBar progressBar = (ProgressBar) findViewById(android.R.id.progress);

        mEmptyView.setText(R.string.internet_error);
        mEmptyView.setButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                mEmptyView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                load();
            }
        });

        String title = getIntent().getStringExtra(IntentExtras.TITLE);

        setTitle(title);

        setProgressBarVisibility(true);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.setVerticalScrollBarEnabled(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                if (progress >= 100)
                {
                    progressBar.setVisibility(View.GONE);

                    mWebView.setVisibility(mError == true ? View.GONE : View.VISIBLE);
                }
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                mWebView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                mError = true;
            }
        });

        load();
    }

    private void load()
    {
        mError = false;

        final String url = getIntent().getStringExtra(IntentExtras.WEB_URL);

        if (url != null)
            mWebView.loadUrl(url);
    }
}
