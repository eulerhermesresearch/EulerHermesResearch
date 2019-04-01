/**
 *
 */

package com.eulerhermes.research.fragment;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.eulerhermes.research.R;
import com.eulerhermes.research.app.IMainActivity;
import com.eulerhermes.research.core.CoreDevice;
import com.eulerhermes.research.network.rest.rss.NewsRequest;
import com.eulerhermes.research.network.rest.rss.RssSpiceService;
import com.eulerhermes.research.util.RequestUtil;
import com.eulerhermes.research.view.News;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Channel;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;
import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.viewpagerindicator.CirclePageIndicator;

public class NewsTabletFragment extends RequestFragment
{
    private final SpiceManager	mSpiceManager;
    private String				mLastRequestCacheKey;
    private IMainActivity		mActivity;
    private JazzyViewPager		mViewPager;
    private NewsAdapter			mAdapter;
    private CirclePageIndicator	mPagerIndicator;

    public static NewsTabletFragment newInstance()
    {
        return new NewsTabletFragment();
    }

    public NewsTabletFragment()
    {
        mSpiceManager = new SpiceManager(RssSpiceService.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_news_tablet, container, false);

        LayoutParams params = view.getLayoutParams();
        params.width = (int) (CoreDevice.getDeviceWidth() / 3);
        view.setLayoutParams(params);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        mViewPager = (JazzyViewPager) view.findViewById(R.id.pager);
        mAdapter = new NewsAdapter(mViewPager);
        mViewPager.setAdapter(mAdapter);

        mPagerIndicator = (CirclePageIndicator) view.findViewById(R.id.pager_indicator);
        mPagerIndicator.setViewPager(mViewPager);

        performRequest();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        LayoutParams params = getView().getLayoutParams();
        params.width = (int) (CoreDevice.getDeviceWidth() / 3);
        getView().setLayoutParams(params);
        getView().invalidate();

        Log.d("NewsTabletFragment", "onConfigurationChanged: ");
    }

    @Override
    public void onStart()
    {
        mSpiceManager.start(getActivity());
        super.onStart();
    }

    @Override
    public void onStop()
    {
        mSpiceManager.shouldStop();
        super.onStop();
    }

    protected SpiceManager getSpiceManager()
    {
        return mSpiceManager;
    }

    @Override
    protected void performRequest()
    {
        super.performRequest();

        NewsRequest request = new NewsRequest();
        mLastRequestCacheKey = request.createCacheKey();

        mSpiceManager.execute(request, mLastRequestCacheKey, RequestUtil.getCacheExpiration(), new RssRequestListener());
    }

    public final class RssRequestListener implements RequestListener<Channel>
    {

        @Override
        public void onRequestFailure(SpiceException spiceException)
        {
            onError();
        }

        @Override
        public void onRequestSuccess(final Channel channel)
        {
            mAdapter.setChannel(channel);

            onLoaded();

            mViewPager.setVisibility(View.VISIBLE);
            mPagerIndicator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        if (activity instanceof IMainActivity)
            this.mActivity = (IMainActivity) activity;
        else
            throw new ClassCastException("Activity must implement IMainActivity");

        super.onAttach(activity);

    }

    @Override
    public void onDetach()
    {
        mActivity = null;
        super.onDetach();
    }

    public static class NewsAdapter extends PagerAdapter
    {
        private JazzyViewPager	mJazzy;
        private List<Item>		news	= new ArrayList<Item>();
        private List<View>		views	= new ArrayList<View>();

        public NewsAdapter(JazzyViewPager viewPager)
        {
            mJazzy = viewPager;
        }

        public void setChannel(Channel channel)
        {
            news = new ArrayList<Item>();

            for (Object o : channel.getItems())
            {
                news.add((Item) o);
            }

            notifyDataSetChanged();
        }

        @Override
        public int getCount()
        {
            return news.size();
        }

        @Override
        public int getItemPosition(Object object)
        {
            int index = news.indexOf(object);
            if (index == -1)
                return POSITION_NONE;
            else
                return index;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position)
        {
            Item item = (Item) news.get(position);
            View view = new News(container.getContext(), item);
            addView(view, position);
            container.addView(view);
            mJazzy.setObjectForPosition(view, position);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }

        // -----------------------------------------------------------------------------
        // Add "view" to right end of "views".
        // Returns the position of the new view.
        // The app should call this to add pages; not used by ViewPager.
        public int addView(View v)
        {
            return addView(v, views.size());
        }

        // -----------------------------------------------------------------------------
        // Add "view" at "position" to "views".
        // Returns position of new view.
        // The app should call this to add pages; not used by ViewPager.
        public int addView(View v, int position)
        {
            views.add(position, v);
            return position;
        }

        // -----------------------------------------------------------------------------
        // Removes "view" from "views".
        // Retuns position of removed view.
        // The app should call this to remove pages; not used by ViewPager.
        public int removeView(ViewPager pager, View v)
        {
            return removeView(pager, views.indexOf(v));
        }

        // -----------------------------------------------------------------------------
        // Removes the "view" at "position" from "views".
        // Retuns position of removed view.
        // The app should call this to remove pages; not used by ViewPager.
        public int removeView(ViewPager pager, int position)
        {
            // ViewPager doesn't have a delete method; the closest is to set the adapter
            // again. When doing so, it deletes all its views. Then we can delete the view
            // from from the adapter and finally set the adapter to the pager again. Note
            // that we set the adapter to null before removing the view from "views" - that's
            // because while ViewPager deletes all its views, it will call destroyItem which
            // will in turn cause a null pointer ref.
            pager.setAdapter(null);
            views.remove(position);
            pager.setAdapter(this);

            return position;
        }

        // -----------------------------------------------------------------------------
        // Returns the "view" at "position".
        // The app should call this to retrieve a view; not used by ViewPager.
        public View getView(int position)
        {
            return views.get(position);
        }
    }
}
