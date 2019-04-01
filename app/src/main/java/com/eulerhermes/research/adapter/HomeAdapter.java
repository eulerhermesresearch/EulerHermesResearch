package com.eulerhermes.research.adapter;

import android.view.View;
import android.view.ViewGroup;
import com.yabeman.android.extended.items.BaseType;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

public class HomeAdapter extends MultiObjectAdapter implements StickyGridHeadersSimpleAdapter {
    public HomeAdapter() {
        this(0);
    }

    public HomeAdapter(int layoutType) {
        super(layoutType);
    }

    public int getViewTypeCount() {
        return 4;
    }

    public long getHeaderId(int position) {
        if (position == 0) {
            return 1;
        }
        return 0;
    }

    public View getHeaderView(int position, View view, ViewGroup parent) {
        HomeHeaderAdapter adapter = new HomeHeaderAdapter();
        if (view == null) {
            view = adapter.createView(parent);
        }
        adapter.setLayoutParams(view);
        adapter.bindData(view, (BaseType) getItem(position), position);
        view.setVisibility(View.VISIBLE);
        if (position == 0) {
            view.setVisibility(View.GONE);
        }
        return view;
    }
}
