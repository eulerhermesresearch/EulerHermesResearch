package com.eulerhermes.research.fragment;

import android.content.Intent;
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
import android.view.ViewStub;
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
import com.eulerhermes.research.common.AnalyticsHelper;
import com.eulerhermes.research.core.CorePrefs;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.core.IntentExtras;
import com.eulerhermes.research.view.EmptyView;
import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;
import com.nhaarman.supertooltips.ToolTipView;

public class RiskMapFragment extends Fragment implements OnTouchListener, OnClickListener {
    public static final String URL = BuildConfig.SERVER_1 + "/map/map3.html";
    private EmptyView mEmptyView;
    private boolean mError;
    private View mFakeView;
    private LayoutInflater mInflater;
    private String mIso;
    private ImageButton mLegendButton;
    private View mToolTipContentView;
    private ToolTipRelativeLayout mToolTipFrameLayout;
    private ToolTipView mToolTipView;
    private WebView mWebView;
    private float mapX;
    private float mapY;

    private class MapWebViewClient extends WebViewClient {
        private MapWebViewClient() {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.equals(RiskMapFragment.URL)) {
                return false;
            }
            mIso = Uri.parse(url).getQueryParameter("iso");
            showToolTip();
            return true;
        }
    }

    public static RiskMapFragment newInstance() {
        return new RiskMapFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mToolTipFrameLayout = (ToolTipRelativeLayout) view.findViewById(R.id.tooltip_frame_layout);
        this.mWebView = (WebView) view.findViewById(R.id.webview);
        this.mEmptyView = (EmptyView) view.findViewById(android.R.id.empty);
        this.mLegendButton = (ImageButton) view.findViewById(R.id.legend_button);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(android.R.id.progress);
        this.mEmptyView.setText((int) R.string.internet_error);
        this.mEmptyView.setButtonListener(new OnClickListener() {
            public void onClick(View v) {
                mEmptyView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                load();
            }
        });
        this.mLegendButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MuPDFActivity.class);
                intent.putExtra(IntentExtras.PDF_LEGEND, true);
                v.getContext().startActivity(intent);
            }
        });
        this.mInflater = LayoutInflater.from(getActivity());
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.getSettings().setBuiltInZoomControls(true);
        this.mWebView.getSettings().setDisplayZoomControls(false);
        this.mWebView.getSettings().setAppCacheEnabled(true);
        this.mWebView.getSettings().setDomStorageEnabled(true);
        this.mWebView.getSettings().setAllowFileAccess(true);
        this.mWebView.getSettings().setLoadsImagesAutomatically(true);
        this.mWebView.getSettings().setAppCachePath(getActivity().getApplicationContext().getCacheDir().getAbsolutePath());
        this.mWebView.setVerticalScrollBarEnabled(true);
        this.mWebView.getSettings().setUseWideViewPort(true);
        this.mWebView.getSettings().setLoadWithOverviewMode(true);
        this.mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        this.mWebView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                int i = 8;
                if (progress >= 100) {
                    int i2;
                    progressBar.setVisibility(View.GONE);
                    ImageButton access$4 = mLegendButton;
                    if (mError) {
                        i2 = 8;
                    } else {
                        i2 = 0;
                    }
                    access$4.setVisibility(i2);
                    WebView access$6 = mWebView;
                    if (!mError) {
                        i = 0;
                    }
                    access$6.setVisibility(i);
                    if (!mError && !CoreUtil.isTablet()) {
                        mWebView.zoomOut();
                    }
                }
            }
        });
        this.mWebView.setWebViewClient(new MapWebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                mWebView.setVisibility(View.GONE);
                mLegendButton.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                mError = true;
            }

            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
                mWebView.scrollTo(Math.round((((float) mWebView.getWidth()) * newScale) / 2.0f), 0);
            }
        });
        this.mWebView.setOnTouchListener(this);
        load();
        if (!CorePrefs.hasShownMapCoachmark()) {
            final View cView = ((ViewStub) view.findViewById(R.id.coachmark)).inflate();
            cView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    cView.setVisibility(View.GONE);
                }
            });
            CorePrefs.setHasShownMapCoachmark();
        }
    }

    private void load() {
        this.mError = false;
        this.mWebView.loadUrl(URL);
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.webview && event.getAction() == 0) {
            this.mapX = event.getX();
            this.mapY = event.getY();
            removeToolTip();
        }
        return false;
    }

    private void showToolTip() {
        ToolTip tt = new ToolTip().withColor(getResources().getColor(R.color.main_blue)).withShadow().withAnimationType(ToolTip.AnimationType.FROM_TOP);
        String country = CoreUtil.getCountryNameForIso(mIso);
        if (country != null) {
            mToolTipContentView = mInflater.inflate(R.layout.view_tooltip, mToolTipFrameLayout, false);
            ((TextView) mToolTipContentView.findViewById(R.id.country)).setText(country);
            ((ImageView) mToolTipContentView.findViewById(R.id.image)).setImageResource(CoreUtil.getCountryFlagForIso(mIso));
            tt.withContentView(mToolTipContentView);

            mFakeView = new View(getActivity());
            mFakeView.setLayoutParams(new LayoutParams(1, 1));
            mFakeView.setX(mapX);
            mFakeView.setY(mapY);
            ((ViewGroup) getView()).addView(mFakeView);

            final int[] masterViewScreenPosition = new int[2];
            mFakeView.getLocationOnScreen(masterViewScreenPosition);

            mToolTipView = mToolTipFrameLayout.showToolTipForView(tt, mFakeView);
            mToolTipView.setOnClickListener(this);
        }
    }

    private void removeToolTip() {
        if (this.mFakeView != null) {
            ((ViewGroup) this.mWebView.getParent()).removeView(this.mFakeView);
            this.mFakeView = null;
        }
        if (this.mToolTipView != null) {
            this.mToolTipFrameLayout.removeAllViews();
            this.mToolTipView = null;
        }
    }

    private void showDialogFragment() {
        CountryFragmentDialog.newInstance(this.mIso).show(getFragmentManager(), "countrydetail");
        sendAnalytics(this.mIso);
    }

    private void sendAnalytics(String country) {
        AnalyticsHelper.event("ui_action", "country_clicked");
    }

    public void onClick(View v) {
        showDialogFragment();
    }

    public void onDestroy() {
        this.mInflater = null;
        this.mToolTipContentView = null;
        this.mToolTipFrameLayout = null;
        this.mToolTipView = null;
        this.mFakeView = null;
        super.onDestroy();
    }
}
