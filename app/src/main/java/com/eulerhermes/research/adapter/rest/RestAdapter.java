package com.eulerhermes.research.adapter.rest;

import android.view.View;
import android.widget.ImageView;
import com.yabeman.android.extended.adapter.TypeAdapter;
import com.yabeman.android.extended.items.BaseType;
import com.eulerhermes.research.imageloader.ImageLoader;

public abstract class RestAdapter extends TypeAdapter<BaseType> {
    public void setLayoutParams(View view) {
    }

    public boolean isEnabled(BaseType object) {
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
