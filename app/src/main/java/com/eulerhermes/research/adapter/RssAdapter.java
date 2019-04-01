package com.eulerhermes.research.adapter;

import com.yabeman.android.extended.adapter.MultiTypeAdapter;
import com.yabeman.android.extended.adapter.TypeAdapter;
import com.crashlytics.android.Crashlytics;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;

public class RssAdapter extends MultiTypeAdapter<Item> {
    public RssAdapter() {
        this(0);
    }

    public RssAdapter(int layoutType) {
        super(layoutType);
    }

    protected TypeAdapter<Item> getAdapter(Item object, int layoutType) {
        TypeAdapter<Item> adapter = AdapterSelector.getAdapter(object, layoutType, this);
        if (adapter == null) {
            Crashlytics.setString("MultiObjectAdapter_object", object != null ? object.toString() : "object is null");
            Crashlytics.setString("MultiObjectAdapter_layout", String.valueOf(layoutType));
        }
        return adapter;
    }

    protected int getItemViewType(Item object) {
        return 0;
    }
}
