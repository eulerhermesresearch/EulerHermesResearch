package com.eulerhermes.research.adapter.holder;

import android.widget.ImageView;
import android.widget.TextView;

public class VideoHolder {
    public final TextView author;
    public final TextView date;
    public final ImageView image;
    public final TextView title;

    public VideoHolder(ImageView image, TextView title, TextView date, TextView author) {
        this.image = image;
        this.title = title;
        this.date = date;
        this.author = author;
    }
}
