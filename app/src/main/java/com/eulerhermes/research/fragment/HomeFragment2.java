package com.eulerhermes.research.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.RssAdapter;
import com.eulerhermes.research.app.IMainActivity;
import com.eulerhermes.research.app.WebActivity;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.network.rest.rss.HomeRequest;
import com.eulerhermes.research.network.rest.rss.RssCategory;
import com.eulerhermes.research.network.rest.rss.RssSpiceService;
import com.eulerhermes.research.util.RequestUtil;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Category;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Channel;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import it.sephiroth.android.library.widget.HListView;

public class HomeFragment2 extends EHHListFragment {
    private IMainActivity activity;
    private String lastRequestCacheKey;
    private final SpiceManager spiceManager = new SpiceManager(RssSpiceService.class);

    public final class RssRequestListener implements RequestListener<Channel> {
        public void onRequestFailure(SpiceException spiceException) {
            HomeFragment2.this.onError();
        }

        public void onRequestSuccess(Channel results) {
            HomeFragment2.this.getAdapter().addAll(results.getItems());
            HomeFragment2.this.getAdapter().notifyDataSetChanged();
            HomeFragment2.this.onLoaded();
        }
    }

    public static HomeFragment2 newInstance() {
        return new HomeFragment2();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(new RssAdapter(2));
        performRequest();
    }

    @Override
    protected void onListItemClick(HListView HListView, View view, int position, long id)
    {

    }

    protected int getCustomLayout() {
        return R.layout.fragment_home;
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

    protected void performRequest() {
        HomeRequest request = new HomeRequest();
        this.lastRequestCacheKey = request.createCacheKey();
        onLoad();
        this.spiceManager.getFromCacheAndLoadFromNetworkIfExpired(request, this.lastRequestCacheKey, RequestUtil.getCacheExpiration(), new RssRequestListener());
    }

    protected RssAdapter getAdapter() {
        return (RssAdapter) getListView().getAdapter();
    }

    protected void onListItemClick(ListView HListView, View view, int position, long id) {
        Item item = (Item) getAdapter().getItem(position);
        if (RssCategory.isApp((Category) item.getCategories().get(0))) {
            this.activity.openFragment(1);
        } else if (RssCategory.isPdf((Category) item.getCategories().get(0))) {
            CoreUtil.openDoc(Doc.fromItem(item), getActivity());
        } else if (RssCategory.isWeb((Category) item.getCategories().get(0))) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("title", item.getLink());
            intent.putExtra("url", item.getLink());
            startActivity(intent);
        }
    }

    public void onAttach(Activity activity) {
        if (activity instanceof IMainActivity) {
            this.activity = (IMainActivity) activity;
            super.onAttach(activity);
            return;
        }
        throw new ClassCastException("Activity must implement IMainActivity");
    }

    public void onDetach() {
        this.activity = null;
        super.onDetach();
    }
}
