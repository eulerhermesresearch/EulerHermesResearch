package com.eulerhermes.research.adapter;

import android.util.Log;
import com.yabeman.android.extended.adapter.TypeAdapter;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.adapter.items.HeaderAdapter;
import com.eulerhermes.research.adapter.items.TitleTextAdapter;
import com.eulerhermes.research.adapter.rest.CountryDetailAdapter;
import com.eulerhermes.research.adapter.rest.DocAdapter;
import com.eulerhermes.research.adapter.rest.DocCategoryAdapter;
import com.eulerhermes.research.adapter.rest.DocDialogAdapter;
import com.eulerhermes.research.adapter.rest.HomeDocAdapter;
import com.eulerhermes.research.adapter.rest.LastDocAdapter;
import com.eulerhermes.research.adapter.rest.LastDocTabletAdapter;
import com.eulerhermes.research.adapter.rss.HomeAdapter;
import com.eulerhermes.research.adapter.rss.TeamAdapter;
import com.eulerhermes.research.adapter.rss.VideoAdapter;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;

public class AdapterSelector {
    static TypeAdapter<BaseType> getAdapter(BaseType object, int layoutType, MultiObjectAdapter parentAdapter) {
        switch (object.getItemViewType()) {
            case 1:
                return new HeaderAdapter();
            case 2:
                return new ProgressAdapter();
            case 6:
                return new TitleTextAdapter();
            case 100:
                if (layoutType == 2) {
                    return new HomeDocAdapter();
                }
                if (layoutType == 4) {
                    return new LastDocAdapter();
                }
                if (layoutType == 6) {
                    return new LastDocTabletAdapter();
                }
                if (layoutType == 5) {
                    return new DocDialogAdapter();
                }
                return new DocAdapter();
            case 101:
                return new DocCategoryAdapter();
            case 102:
                return new CountryDetailAdapter();
            case 103:
                return new HomeHorizontalListHeaderAdapter();
            default:
                return null;
        }
    }

    static TypeAdapter<Item> getAdapter(Item object, int layoutType, RssAdapter parentAdapter) {
        switch (layoutType) {
            case 1:
                return new VideoAdapter();
            case 2:
                return new HomeAdapter();
            case 3:
                return new TeamAdapter();
            default:
                Log.e("AdapterSelector", "No adapter defined for layout type " + layoutType);
                return null;
        }
    }
}
