package com.eulerhermes.research.adapter.rss;

import android.view.View;
import android.widget.ImageView;
import com.eulerhermes.research.R;
import com.eulerhermes.research.core.CoreUtil;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;

public class HomeAdapter extends RssItemAdapter {
    protected int getLayoutRes() {
        return R.layout.item_home;
    }

    public void bindData(View view, Item data, int position) {
        loadImage((ImageView) view, data.getSource().getValue(), CoreUtil.getImagePlaceholder());
    }

    protected void attachViewHolder(View view) {
    }
}
