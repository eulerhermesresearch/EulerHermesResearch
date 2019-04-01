package com.eulerhermes.research.adapter.holder;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class TeamHolder {
    public final TextView desc;
    public final ImageView image;
    public final ImageButton overflow;
    public final TextView title;

    public TeamHolder(ImageView image, ImageButton overflow, TextView title, TextView desc) {
        this.image = image;
        this.overflow = overflow;
        this.title = title;
        this.desc = desc;
    }
}
