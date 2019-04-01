package com.eulerhermes.research.imageloader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.squareup.picasso.Picasso.LoadedFrom;

public class SimpleFakeImageLoaderListener implements FakeImageLoaderListener {
    public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable)
    {

    }

    public void onPrepareLoad(Drawable drawable) {
    }
}
