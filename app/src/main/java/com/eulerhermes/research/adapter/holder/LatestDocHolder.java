package com.eulerhermes.research.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;

public class LatestDocHolder {
    public final TextView date;
    public final TextView desc;
    public final ImageView image;
    public final TextView title;

    public LatestDocHolder(ImageView image, TextView title, TextView desc, TextView date) {
        this.image = image;
        this.title = title;
        this.desc = desc;
        this.date = date;
    }
}
