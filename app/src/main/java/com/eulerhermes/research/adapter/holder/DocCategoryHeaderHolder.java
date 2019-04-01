package com.eulerhermes.research.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DocCategoryHeaderHolder {
    public final View border;
    public final TextView desc;
    public final ImageView image;

    public DocCategoryHeaderHolder(ImageView image, TextView desc, View border) {
        this.image = image;
        this.desc = desc;
        this.border = border;
    }
}
