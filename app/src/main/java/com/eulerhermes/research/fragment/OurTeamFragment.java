/**
 *
 */

package com.eulerhermes.research.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import com.eulerhermes.research.R;
import com.eulerhermes.research.adapter.LayoutType;
import com.eulerhermes.research.adapter.RssOurTeamAdapter;
import com.eulerhermes.research.network.rest.rss.OurTeamRequest;
import com.eulerhermes.research.network.rest.rss.RssSpiceService;
import com.eulerhermes.research.util.RequestUtil;
import com.eulerhermes.research.view.EmptyView;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Category;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Channel;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

public class OurTeamFragment extends EHGridFragment
{
    private final SpiceManager	spiceManager;
    private String				lastRequestCacheKey;

    public OurTeamFragment()
    {
        spiceManager = new SpiceManager(RssSpiceService.class);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        setAdapter(new RssOurTeamAdapter(LayoutType.OUR_TEAM));

        //Cannot use default empty view behavior because of MaxLayout
        EmptyView emptyView = (EmptyView) view.findViewById(android.R.id.empty);
        emptyView.setButtonListener(new OnClickListener() {

            @Override
            public void onClick(View v)
            {
                onLoad();
                performRequest();
            }
        });

        getGridView().setEmptyView(emptyView);

        performRequest();
    }

    @Override
    protected void onGridItemClick(GridView gridView, View view, int position, long id)
    {
        // final Item item = getAdapter().getItem(position);
    }

    @Override
    protected int getCustomLayout()
    {
        return R.layout.fragment_our_team;
    }

    @Override
    protected View getEmptyView()
    {
        return null;
    }

    @Override
    protected int getColumnCount()
    {
        return 1;
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

    protected SpiceManager getSpiceManager()
    {
        return spiceManager;
    }

    @Override
    protected void performRequest()
    {
        OurTeamRequest request = new OurTeamRequest();
        lastRequestCacheKey = request.createCacheKey();

        onLoad();
        spiceManager.getFromCacheAndLoadFromNetworkIfExpired(request, lastRequestCacheKey, RequestUtil.getCacheExpiration(), new RssRequestListener());
    }

    @Override
    protected RssOurTeamAdapter getAdapter()
    {
        return (RssOurTeamAdapter) getGridView().getAdapter();
    }

    public final class RssRequestListener implements RequestListener<Channel>
    {

        @Override
        public void onRequestFailure(SpiceException spiceException)
        {
            onError();
        }

        @Override
        public void onRequestSuccess(final Channel results)
        {
            String category = "";
            for (Object object : results.getItems())
            {
                Item item = (Item) object;

                if (item.getCategories().size() > 0)
                {
                    String itemCategory = ((Category) item.getCategories().get(0)).getValue();

                    if (!category.equals(itemCategory))
                    {
                        Item i = new Item();
                        i.setCategories(item.getCategories());
                        getAdapter().add(i);

                        category = itemCategory;
                    }
                }

                getAdapter().add(item);
            }
            getAdapter().notifyDataSetChanged();
            onLoaded();
        }
    }
}
