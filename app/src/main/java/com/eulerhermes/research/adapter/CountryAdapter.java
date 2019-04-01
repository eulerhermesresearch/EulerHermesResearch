package com.eulerhermes.research.adapter;

import android.view.View;
import android.view.ViewGroup;
import com.eulerhermes.research.model.CountryDetail;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

public class CountryAdapter extends MultiObjectAdapter implements StickyGridHeadersSimpleAdapter {
    private CountryDetail detail;

    public CountryAdapter() {
        this(0);
    }

    public CountryAdapter(int layoutType) {
        super(layoutType);
    }

    public long getHeaderId(int position) {
        return 0;
    }

    public View getHeaderView(int position, View view, ViewGroup parent) {
        CountryHeaderAdapter adapter = new CountryHeaderAdapter();
        if (view == null) {
            view = adapter.createView(parent);
        }
        adapter.setLayoutParams(view);
        adapter.bindData(view, this.detail, position);
        return view;
    }

    public void setCountryDetail(CountryDetail detail) {
        this.detail = detail;
    }
}
