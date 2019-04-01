package com.eulerhermes.research.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.artifex.mupdfdemo.MuPDFActivity;
import com.eulerhermes.research.BuildConfig;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.core.IntentExtras;
import com.eulerhermes.research.view.EmptyView;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;

public class CollectionMapFragment extends Fragment implements OnTouchListener, OnClickListener
{
    public static final String                TAG = RiskMapFragment.class.getSimpleName();
    public static final String                URL = BuildConfig.SERVER_2 + "/map/ehc.html";
    private             EmptyView             mEmptyView;
    private             boolean               mError;
    private             View                  mFakeView;
    private             LayoutInflater        mInflater;
    private             String                mIso;
    private             ImageView             mLegend;
    private             ImageButton           mLegendButton;
    private             View                  mToolTipContentView;
    private             ToolTipRelativeLayout mToolTipFrameLayout;
    private             ToolTipView           mToolTipView;
    private             WebView               mWebView;
    private             float                 mapX;
    private             float                 mapY;

    private class MapWebViewClient extends WebViewClient
    {
        private MapWebViewClient()
        {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
            if (url.equals(CollectionMapFragment.URL)) {
                return false;
            }
            CollectionMapFragment.this.mIso = Uri.parse(url).getQueryParameter("iso");
            CollectionMapFragment.this.showToolTip();
            return true;
        }
    }

    public static CollectionMapFragment newInstance()
    {
        return new CollectionMapFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d("MainActivity", "onCreate: " + this);
        return inflater.inflate(R.layout.fragment_collection_map, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        this.mToolTipFrameLayout = (ToolTipRelativeLayout) view.findViewById(R.id.tooltip_frame_layout);
        this.mWebView = (WebView) view.findViewById(R.id.webview);
        this.mEmptyView = (EmptyView) view.findViewById(android.R.id.empty);
        this.mLegendButton = (ImageButton) view.findViewById(R.id.legend_button);
        this.mLegend = (ImageView) view.findViewById(R.id.map_legend);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(android.R.id.progress);
        this.mEmptyView.setText((int) R.string.internet_error);
        this.mEmptyView.setButtonListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                CollectionMapFragment.this.mEmptyView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                CollectionMapFragment.this.load();
            }
        });
        this.mLegendButton.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), MuPDFActivity.class);
                intent.putExtra(IntentExtras.PDF_LEGEND, true);
                v.getContext().startActivity(intent);
            }
        });
        this.mInflater = LayoutInflater.from(getContext());
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setBuiltInZoomControls(true);
        this.mWebView.getSettings().setDisplayZoomControls(false);
        this.mWebView.getSettings().setAppCacheEnabled(true);
        this.mWebView.getSettings().setDomStorageEnabled(true);
        this.mWebView.getSettings().setAllowFileAccess(true);
        this.mWebView.getSettings().setLoadsImagesAutomatically(true);
        this.mWebView.getSettings().setAppCachePath(getActivity().getApplicationContext().getCacheDir().getAbsolutePath());
        this.mWebView.setVerticalScrollBarEnabled(true);
        this.mWebView.getSettings().setUseWideViewPort(false);
        this.mWebView.getSettings().setLoadWithOverviewMode(false);
        this.mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        this.mWebView.setWebChromeClient(new WebChromeClient()
        {
            public void onProgressChanged(WebView view, int progress)
            {
                if (progress >= 100) {
                    mWebView.postDelayed(new Runnable()
                    {
                        public void run()
                        {
                            progressBar.setVisibility(View.GONE);

                            mLegendButton.setVisibility(mError ? View.GONE : View.VISIBLE);
                            mLegend.setVisibility(mError ? View.GONE : View.VISIBLE);

                            if (!CollectionMapFragment.this.mError) {
                                mWebView.setVisibility(View.VISIBLE);
                            }
                        }
                    }, 4000);
                }
            }
        });

        this.mWebView.setWebViewClient(new MapWebViewClient()
        {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                mWebView.setVisibility(View.GONE);
                mLegendButton.setVisibility(View.GONE);
                mLegend.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                mError = true;
            }

            public void onScaleChanged(WebView view, float oldScale, float newScale)
            {
//                Log.d("RiskMapFragment.onViewCreated(...).new MapWebViewClient() {...}", "onScaleChanged: " + oldScale + " " + newScale);
                super.onScaleChanged(view, oldScale, oldScale);
            }
        });
        this.mWebView.setOnTouchListener(this);
        load();
    }

    private void load()
    {
        this.mError = false;
        this.mWebView.loadUrl(URL);
    }

    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
    }

    public boolean onTouch(View v, MotionEvent event)
    {
        if (v.getId() == R.id.webview && event.getAction() == 0) {
            this.mapX = event.getX();
            this.mapY = event.getY();
            removeToolTip();
        }
        return false;
    }

    private void showToolTip()
    {
        ToolTip tt      = new ToolTip().withColor(getResources().getColor(R.color.tooltip_bgd_color)).withShadow();
        String  country = CoreUtil.getCountryNameForIso(this.mIso);
        if (country != null) {
            this.mToolTipContentView = this.mInflater.inflate(R.layout.view_tooltip, this.mToolTipFrameLayout, false);
            ((TextView) this.mToolTipContentView.findViewById(R.id.country)).setText(country);
            ((ImageView) this.mToolTipContentView.findViewById(R.id.image)).setImageResource(CoreUtil.getCountryFlagForIso(this.mIso));
            tt.withContentView(this.mToolTipContentView);
            this.mFakeView = new View(getActivity());
            this.mFakeView.setLayoutParams(new LayoutParams(1, 1));
            this.mFakeView.setX(this.mapX);
            this.mFakeView.setY(this.mapY);
            ((ViewGroup) this.mWebView.getParent()).addView(this.mFakeView);
            this.mToolTipView = this.mToolTipFrameLayout.showToolTipForView(tt, this.mFakeView);
            this.mToolTipView.setOnClickListener(this);
        }
    }

    private void removeToolTip()
    {
        if (this.mFakeView != null) {
            ((ViewGroup) this.mWebView.getParent()).removeView(this.mFakeView);
            this.mFakeView = null;
        }
        if (this.mToolTipView != null) {
            this.mToolTipFrameLayout.removeAllViews();
            this.mToolTipView = null;
        }
    }

    private void showDialogFragment()
    {
        CountryEHCFragmentDialog.newInstance(this.mIso).show(getActivity().getSupportFragmentManager(), "country_ehc_dialog");
    }

    public void onClick(View v)
    {
        showDialogFragment();
    }

    public void onDestroy()
    {
        this.mInflater = null;
        this.mToolTipContentView = null;
        this.mToolTipFrameLayout = null;
        this.mToolTipView = null;
        this.mFakeView = null;
        super.onDestroy();
    }
}
