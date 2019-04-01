package com.eulerhermes.research.app;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import com.eulerhermes.research.BuildConfig;
import com.yabeman.android.extended.util.AlertUtil;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.view.EmptyView;

public class TermsActivity extends Activity
{
    private static final String    MENTIONS_URL = BuildConfig.SERVER_1 + "/client/web/mentions.htm";
    private              EmptyView mEmptyView;
    private              boolean   mError;
    private              WebView   mWebView;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setHomeButtonEnabled(false);
        getActionBar().setTitle("General Terms of Use");
        this.mWebView = (WebView) findViewById(R.id.webview);
        this.mEmptyView = (EmptyView) findViewById(android.R.id.empty);
        final ProgressBar progressBar = (ProgressBar) findViewById(android.R.id.progress);
        this.mEmptyView.setText(R.string.internet_error);
        this.mEmptyView.setButtonListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                TermsActivity.this.mEmptyView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                TermsActivity.this.load();
            }
        });
        setProgressBarVisibility(true);
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setBuiltInZoomControls(true);
        this.mWebView.getSettings().setDisplayZoomControls(false);
        this.mWebView.setVerticalScrollBarEnabled(true);
        this.mWebView.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view, int progress)
            {
                int i = 8;
                if (progress >= 100) {
                    progressBar.setVisibility(View.GONE);
                    WebView access$2 = TermsActivity.this.mWebView;
                    if (!TermsActivity.this.mError) {
                        i = 0;
                    }
                    access$2.setVisibility(i);
                }
            }
        });
        this.mWebView.setWebViewClient(new WebViewClient()
        {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                TermsActivity.this.mWebView.setVisibility(View.GONE);
                TermsActivity.this.mEmptyView.setVisibility(View.VISIBLE);
                TermsActivity.this.mError = true;
            }
        });
        Button negativeButton = (Button) findViewById(R.id.negative_button);
        ((Button) findViewById(R.id.positive_button)).setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                CorePrefs.setHasAcceptedTermsAndConditions();
                TermsActivity.this.startActivity(new Intent(TermsActivity.this, MainActivity.class));
                TermsActivity.this.finish();
            }
        });
        negativeButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                TermsActivity.this.showWarningDialogAndCloseApp();
            }
        });
        load();
    }

    private void load()
    {
        this.mError = false;
        this.mWebView.loadUrl(MENTIONS_URL);
    }

    private void showWarningDialogAndCloseApp()
    {
        AlertUtil.showAlert((Context) this, R.string.disclaimer, R.string.terms_of_use_refusal, R.string.ok, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                TermsActivity.this.finish();
            }
        }).setOnDismissListener(new OnDismissListener()
        {
            public void onDismiss(DialogInterface dialog)
            {
                TermsActivity.this.finish();
            }
        });
    }
}
