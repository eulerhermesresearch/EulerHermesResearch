package com.eulerhermes.research.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.eulerhermes.research.R;
import com.eulerhermes.research.app.IMainActivity;
import com.eulerhermes.research.app.WebActivity;
import com.eulerhermes.research.core.CoreUtil;
import com.eulerhermes.research.imageloader.ImageLoader;
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

public class HomeTabletFragment extends RequestFragment implements OnClickListener {
    private IMainActivity activity;
    private Channel channel;
    private ImageView[] images;
    private String lastRequestCacheKey;
    private final SpiceManager spiceManager = new SpiceManager(RssSpiceService.class);

    public final class HomeRequestListener implements RequestListener<Channel> {
        public void onRequestFailure(SpiceException spiceException) {
            HomeTabletFragment.this.onError();
        }

        public void onRequestSuccess(Channel channel) {
            HomeTabletFragment.this.channel = channel;
            int n = channel.getItems().size();
            for (int i = 0; i < n; i++) {
                ImageLoader.display(HomeTabletFragment.this.images[i], ((Item) channel.getItems().get(i)).getSource().getValue(), CoreUtil.getImagePlaceholder());
            }
            HomeTabletFragment.this.onLoaded();
        }
    }

    public static HomeTabletFragment newInstance() {
        return new HomeTabletFragment();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home_tablet, container, false);
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView image1 = (ImageView) view.findViewById(R.id.image1);
        ImageView image2 = (ImageView) view.findViewById(R.id.image2);
        ImageView image3 = (ImageView) view.findViewById(R.id.image3);
        ImageView image4 = (ImageView) view.findViewById(R.id.image4);
        ImageView image5 = (ImageView) view.findViewById(R.id.image5);
        ImageView image6 = (ImageView) view.findViewById(R.id.image6);
        this.images = new ImageView[]{image1, image2, image3, image4, image5, image6};
        view.findViewById(R.id.button1).setOnClickListener(this);
        view.findViewById(R.id.button2).setOnClickListener(this);
        view.findViewById(R.id.button3).setOnClickListener(this);
        view.findViewById(R.id.button4).setOnClickListener(this);
        view.findViewById(R.id.button5).setOnClickListener(this);
        view.findViewById(R.id.button6).setOnClickListener(this);
        performRequest();
    }

    public void onStart() {
        this.spiceManager.start(getActivity());
        super.onStart();
    }

    public void onStop() {
        this.spiceManager.shouldStop();
        super.onStop();
    }

    public void onDestroy() {
        this.images = null;
        super.onDestroy();
    }

    protected SpiceManager getSpiceManager() {
        return this.spiceManager;
    }

    protected void performRequest() {
        super.performRequest();
        HomeRequest request = new HomeRequest();
        this.lastRequestCacheKey = request.createCacheKey();
        this.spiceManager.getFromCacheAndLoadFromNetworkIfExpired(request, this.lastRequestCacheKey, RequestUtil.getCacheExpiration(), new HomeRequestListener());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                onButtonClick(0);
                return;
            case R.id.button4:
                onButtonClick(3);
                return;
            case R.id.button5:
                onButtonClick(4);
                return;
            case R.id.button2:
                onButtonClick(1);
                return;
            case R.id.button3:
                onButtonClick(2);
                return;
            case R.id.button6:
                onButtonClick(5);
                return;
            default:
                return;
        }
    }

    private void onButtonClick(int button) {
        Item item = (Item) this.channel.getItems().get(button);
        if (RssCategory.isApp((Category) item.getCategories().get(0))) {
            this.activity.openFragment(1);
        } else if (RssCategory.isPdf((Category) item.getCategories().get(0))) {
            CoreUtil.openDoc(Doc.fromItem(item), getActivity());
        } else if (RssCategory.isWeb((Category) item.getCategories().get(0))) {
            Intent intent = new Intent(getActivity(), WebActivity.class);
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
