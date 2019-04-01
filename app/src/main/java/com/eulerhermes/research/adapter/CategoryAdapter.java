package com.eulerhermes.research.adapter;

import android.view.View;
import android.view.ViewGroup;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.adapter.rest.DocCategoryAdapter;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

public class CategoryAdapter extends MultiObjectAdapter implements StickyGridHeadersSimpleAdapter {
    public CategoryAdapter() {
        this(0);
    }

    public CategoryAdapter(int layoutType) {
        super(layoutType);
    }

    public int getViewTypeCount() {
        return 2;
    }

    public long getHeaderId(int position) {
        return 1;
    }

    public View getHeaderView(int position, View view, ViewGroup parent) {
        DocCategoryAdapter adapter = new DocCategoryAdapter();
        if (view == null) {
            view = adapter.createView(parent);
        }
        adapter.setLayoutParams(view);
        adapter.bindData(view, (BaseType) getItem(position), position);
        return view;
    }
}
