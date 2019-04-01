package com.eulerhermes.research.adapter.rss;

import android.view.View;
import android.widget.ImageView;
import com.yabeman.android.extended.adapter.TypeAdapter;
import com.eulerhermes.research.imageloader.ImageLoader;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Item;

public abstract class RssItemAdapter extends TypeAdapter<Item> {
    public void setLayoutParams(View view) {
    }

    public boolean isEnabled(Item object) {
        return true;
    }

    protected void loadImage(ImageView imageView, String url) {
        ImageLoader.display(imageView, url);
    }

    protected void loadImage(ImageView imageView, String url, int placeHolder) {
        ImageLoader.display(imageView, url, placeHolder);
    }

    protected void loadImage(ImageView imageView, String url, boolean fadeIn) {
        ImageLoader.display(imageView, url, fadeIn);
    }

    protected void loadImage(ImageView imageView, String url, int placeHolder, boolean fadeIn) {
        ImageLoader.display(imageView, url, fadeIn, placeHolder);
    }
}
