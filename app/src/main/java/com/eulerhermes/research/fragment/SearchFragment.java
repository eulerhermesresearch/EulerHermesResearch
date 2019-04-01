package com.eulerhermes.research.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.MultiObjectAdapter;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.model.Docs;
import com.eulerhermes.research.network.rest.SearchRequest;
import com.eulerhermes.research.util.RequestUtil;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchFragment extends EHGridFragment {
    private MultiObjectAdapter adapter;
    private String lastRequestCacheKey;
    private String query;
    private final SpiceManager spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);

    public final class SearchListener implements RequestListener<Docs> {
        public void onRequestFailure(SpiceException spiceException) {
            SearchFragment.this.onError();
        }

        public void onRequestSuccess(Docs docs) {
            List<String> tags = new ArrayList();
            int n = docs.size();
            for (int i = 0; i < n; i++) {
                for (String tag : ((Doc) docs.get(i)).getTagArray()) {
                    if (!tags.contains(tag)) {
                        tags.add(tag);
                    }
                }
            }
            Collections.sort(tags);
            List<BaseType> data = new ArrayList();
            data.addAll(docs);
            SearchFragment.this.getAdapter().setData(data);
            SearchFragment.this.getAdapter().notifyDataSetChanged();
            SearchFragment.this.onLoaded();
        }
    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.adapter = new MultiObjectAdapter();
        setAdapter(this.adapter);
        ((StickyGridHeadersGridView) getGridView()).setAreHeadersSticky(false);
        if (this.query != null) {
            performRequest();
        }
    }

    protected void onGridItemClick(GridView gridView, View view, int position, long id) {
        CoreUtil.openDoc((Doc) getAdapter().getItem(position), getActivity());
    }

    protected int getCustomLayout() {
        return R.layout.grid_sticky_headers;
    }

    protected int getColumnCount() {
        return getResources().getInteger(R.integer.grid_column_count);
    }

    public void onStart() {
        this.spiceManager.start(getActivity());
        super.onStart();
    }

    public void onStop() {
        this.spiceManager.shouldStop();
        super.onStop();
    }

    protected void onError() {
        getAdapter().clear();
        super.onError();
    }

    protected SpiceManager getSpiceManager() {
        return this.spiceManager;
    }

    public void setQuery(String query) {
        this.query = query;
        if (isAdded()) {
            performRequest();
        }
    }

    protected void performRequest() {
        SearchRequest request = new SearchRequest(this.query);
        this.lastRequestCacheKey = request.createCacheKey();
        onLoad();
        this.spiceManager.getFromCacheAndLoadFromNetworkIfExpired(request, this.lastRequestCacheKey, RequestUtil.getCacheExpiration(), new SearchListener());
    }

    protected MultiObjectAdapter getAdapter() {
        return this.adapter;
    }
}
