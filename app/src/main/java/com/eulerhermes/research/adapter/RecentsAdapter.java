package com.eulerhermes.research.adapter;

import android.view.View;
import android.view.ViewGroup;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.model.Doc;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

public class RecentsAdapter extends MultiObjectAdapter implements StickyGridHeadersSimpleAdapter {
    public RecentsAdapter() {
        this(0);
    }

    public RecentsAdapter(int layoutType) {
        super(layoutType);
    }

    public int getViewTypeCount() {
        return 2;
    }

    public long getHeaderId(int position) {
        return (long) ((Doc) getItem(position)).getIntCategory();
    }

    public View getHeaderView(int position, View view, ViewGroup parent) {
        RecentsHeaderAdapter adapter = new RecentsHeaderAdapter();
        if (view == null) {
            view = adapter.createView(parent);
        }
        adapter.setLayoutParams(view);
        adapter.bindData(view, (BaseType) getItem(position), position);
        return view;
    }
}
