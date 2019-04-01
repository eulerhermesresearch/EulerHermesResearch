package com.eulerhermes.research.adapter.holder;

import android.view.View;
import android.widget.HorizontalScrollView;

public class HomeHorizontalListHolder {
    public final View item1;
    public final View item2;
    public final View item3;
    public final View item4;
    public final HorizontalScrollView scrollView;

    public HomeHorizontalListHolder(HorizontalScrollView scrollView, View item1, View item2, View item3, View item4) {
        this.scrollView = scrollView;
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
    }
}
