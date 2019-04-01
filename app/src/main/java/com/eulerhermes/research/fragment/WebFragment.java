/**
 *
 */

package com.eulerhermes.research.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import androidx.fragment.app.Fragment;
import com.eulerhermes.research.R;
import com.eulerhermes.research.view.EmptyView;

public class WebFragment extends Fragment
{
    public static final String	URL	= "com.eulerhermes.research.fragment.WebFragment.URL";

    private WebView				mWebView;
    private EmptyView			mEmptyView;
    private boolean				mError;

    public WebFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_web, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mWebView = (WebView) view.findViewById(R.id.webview);
        mEmptyView = (EmptyView) view.findViewById(android.R.id.empty);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(android.R.id.progress);

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
                // Toast.makeText(getActivity(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                mWebView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                mError = true;
            }
        });

        if (getArguments() != null && getArguments().getString(URL) != null)
        {
            mWebView.loadUrl(getArguments().getString(URL));
        }
        else if (getUrl() != null)
        {
            mWebView.loadUrl(getUrl());
        }
        else
        {
            Log.d("WebFragment", "onViewCreated: no url to load");
        }
    }

    private void load()
    {
        mError = false;

        if (getArguments() != null && getArguments().getString(URL) != null)
        {
            mWebView.loadUrl(getArguments().getString(URL));
        }
        else if (getUrl() != null)
        {
            mWebView.loadUrl(getUrl());
        }
        else
        {
            Log.d("WebFragment", "onViewCreated: no url to load");
        }
    }

    protected String getUrl()
    {
        return null;
    }
}
