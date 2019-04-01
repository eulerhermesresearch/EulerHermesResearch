package com.eulerhermes.research.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;

public class HomeDocHolder {
    public final TextView date;
    public final TextView desc;
    public final ImageView icon;
    public final TextView title;

    public HomeDocHolder(ImageView icon, TextView title, TextView desc, TextView date) {
        this.icon = icon;
        this.title = title;
        this.desc = desc;
        this.date = date;
    }
}
