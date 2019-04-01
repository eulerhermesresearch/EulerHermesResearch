package com.eulerhermes.research.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.MultiObjectAdapter;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.model.Docs;
import com.eulerhermes.research.network.rest.LastDocsSmartphoneRequest;
import com.eulerhermes.research.util.RequestUtil;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import it.sephiroth.android.library.widget.HListView;

import java.util.Iterator;

public class LastDocsFragment extends EHHListFragment implements IRefreshFragment {
    private String lastRequestCacheKey;
    private final SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

    public final class LatestDocsRequestListener implements RequestListener<Docs> {
        public void onRequestFailure(SpiceException spiceException) {
            LastDocsFragment.this.onError();
        }

        public void onRequestSuccess(Docs results) {
            Iterator it = results.iterator();
            while (it.hasNext()) {
                LastDocsFragment.this.getAdapter().add((Doc) it.next());
            }
            LastDocsFragment.this.getAdapter().notifyDataSetChanged();
            LastDocsFragment.this.onLoaded();
        }
    }

    public static LastDocsFragment newInstance() {
        return new LastDocsFragment();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(new MultiObjectAdapter(4));
        performRequest();
    }

    @Override
    protected void onListItemClick(HListView HListView, View view, int position, long id)
    {

    }

    protected int getCustomLayout() {
        return R.layout.fragment_last_docs;
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
        LastDocsSmartphoneRequest request = new LastDocsSmartphoneRequest();
        this.lastRequestCacheKey = request.createCacheKey();
        onLoad();
        this.spiceManager.getFromCacheAndLoadFromNetworkIfExpired(request, this.lastRequestCacheKey, RequestUtil.getCacheExpiration(), new LatestDocsRequestListener());
    }

    protected MultiObjectAdapter getAdapter() {
        return (MultiObjectAdapter) getListView().getAdapter();
    }

    protected void onListItemClick(ListView HListView, View view, int position, long id) {
        CoreUtil.openDoc((Doc) getAdapter().getItem(position), getActivity());
    }

    public void refresh() {
        getAdapter().notifyDataSetChanged();
    }
}
