package com.eulerhermes.research.adapter.holder;

import android.widget.TextView;

public class DocHolder {
    public final TextView date;
    public final TextView desc;
    public final TextView title;

    public DocHolder(TextView title, TextView desc, TextView date) {
        this.title = title;
        this.desc = desc;
        this.date = date;
    }
}
