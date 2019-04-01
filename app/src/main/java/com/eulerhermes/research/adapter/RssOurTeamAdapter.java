package com.eulerhermes.research.adapter;

import com.yabeman.android.extended.adapter.MultiTypeAdapter;
import com.yabeman.android.extended.adapter.TypeAdapter;
import com.eulerhermes.research.adapter.rss.RssHeaderAdapter;
import com.eulerhermes.research.adapter.rss.TeamAdapter;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;

public class RssOurTeamAdapter extends MultiTypeAdapter<Item> {
    public RssOurTeamAdapter() {
        this(0);
    }

    public RssOurTeamAdapter(int layoutType) {
        super(layoutType);
    }

    protected TypeAdapter<Item> getAdapter(Item object, int layoutType) {
        if (object.getTitle() == null) {
            return new RssHeaderAdapter();
        }
        return new TeamAdapter();
    }

    public int getViewTypeCount() {
        return 2;
    }

    protected int getItemViewType(Item object) {
        return object.getTitle() == null ? 0 : 1;
    }
}
