/**
 *
 */

package com.eulerhermes.research.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import com.yabeman.android.extended.adapter.MultiTypeAdapter;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.MultiObjectAdapter;
import com.eulerhermes.research.app.WebActivity;
import com.eulerhermes.research.model.Doc;
import com.eulerhermes.research.model.Docs;
import com.eulerhermes.research.network.rest.VideosRequest;
import com.octo.android.robospice.Jackson2GoogleHttpClientSpiceService;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;

public class VideosFragment extends EHPagedGridFragment
{
    private SpiceManager		spiceManager;
    private String				lastRequestCacheKey;
    private MultiObjectAdapter adapter;
    private int page = 1;

    public VideosFragment()
    {
        spiceManager = new SpiceManager(Jackson2GoogleHttpClientSpiceService.class);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MultiObjectAdapter(2);
        setAdapter(adapter);
        getGridView().setPadding(0, 0, 0, 0);
        getGridView().setVerticalSpacing(1);

        onLoad();
        performRequest();
    }

    @Override
    protected void onGridItemClick(GridView gridView, View view, int position, long id)
    {
        Doc doc = (Doc) getAdapter().getItem(position);
        if (doc.isVideo()) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("url", doc.getLink());
            intent.putExtra("title", doc.getTitle());
            getActivity().startActivity(intent);
            return;
        }
        /*
        VideoFragmentDialog vfd = VideoFragmentDialog.newInstance(item);
        vfd.show(getFragmentManager(), "VideoDialog");
        */
    }

    @Override
    protected int getColumnCount()
    {
        return 1;
    }

    @Override
    protected int getCustomLayout()
    {
        return R.layout.grid;
    }

    @Override
    public void onStart()
    {
        spiceManager.start(getActivity());
        super.onStart();
    }

    @Override
    public void onStop()
    {
        spiceManager.shouldStop();
        super.onStop();
    }

    @Override
    public void onDestroy()
    {
        spiceManager = null;
        super.onDestroy();
    }

    protected SpiceManager getSpiceManager()
    {
        return spiceManager;
    }

    @Override
    protected MultiTypeAdapter<BaseType> getAdapter()
    {
        return adapter;
    }

    @Override
    protected void loadMore()
    {
        startLoading();
        performRequest();
    }

    @Override
    protected void performRequest()
    {
        VideosRequest request = new VideosRequest(page);
        lastRequestCacheKey = request.createCacheKey();

        onLoad();
        spiceManager.getFromCacheAndLoadFromNetworkIfExpired(request, lastRequestCacheKey, DurationInMillis.ONE_SECOND/*RequestUtil.getCacheExpiration()*/, new VideoRequestListener());
    }

    public final class VideoRequestListener implements RequestListener<Docs>
    {

        @Override
        public void onRequestFailure(SpiceException spiceException)
        {
            Log.d("VideosFragment", "error " + spiceException.getMessage());
            Log.d("VideosFragment", "error " + spiceException);
            onError();
        }

        @Override
        public void onRequestSuccess(final Docs docs)
        {
            getAdapter().addAll(new ArrayList<BaseType>(docs));
            getAdapter().notifyDataSetChanged();
            onLoaded();
            endLoading();
            if (docs.size() == 0)
                setNoMoreData(true);

            page++;
        }
    }
}
