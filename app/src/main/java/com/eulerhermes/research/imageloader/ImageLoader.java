package com.eulerhermes.research.imageloader;

import android.content.Context;
import android.net.Uri;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import com.eulerhermes.research.core.BaseApplication;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

public class ImageLoader {
    protected static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    public static boolean FADE_ENABLED = true;
    public static final String FAKE_URI = "http://www.abc.com/12398752.jpg";

    public static void initImageLoader(Context context) {
    }

    public static void display(ImageView imageView, int resId) {
        display(imageView, resId, 0);
    }

    public static void display(ImageView imageView, int resId, int stubImage) {
        display(imageView, resId, 0, null);
    }

    public static void display(ImageView imageView, int resId, int stubImage, ImageLoaderListener listener) {
        RequestCreator requestCreator = Picasso.get().load(resId);
        if (stubImage != 0) {
            requestCreator.placeholder(stubImage);
            requestCreator.error(stubImage);
        }
        LayoutParams params = imageView.getLayoutParams();
        if (params.width > 0 && params.height > 0) {
            requestCreator.resize(params.width, params.height);
        }
        requestCreator.onlyScaleDown();
        requestCreator.into(imageView, listener);
    }

    public static void display(ImageView imageView, String uri) {
        display(imageView, uri, false, 0, null);
    }

    public static void display(ImageView imageView, String uri, boolean fadeIn) {
        display(imageView, uri, fadeIn, 0, null);
    }

    public static void display(ImageView imageView, String uri, int stubImage) {
        display(imageView, uri, false, stubImage, null);
    }

    public static void display(ImageView imageView, String uri, boolean fadeIn, int stubImage) {
        display(imageView, uri, fadeIn, stubImage, null);
    }

    public static void display(ImageView imageView, String uri, ImageLoaderListener listener) {
        display(imageView, uri, false, 0, listener);
    }

    public static void display(ImageView imageView, String uri, boolean fadeIn, int stubImage, ImageLoaderListener listener) {
        if (uri == null || uri.length() == 0) {
            uri = FAKE_URI;
        }
        RequestCreator requestCreator = Picasso.get().load(Uri.encode(uri, ALLOWED_URI_CHARS));
        if (stubImage != 0) {
            requestCreator.placeholder(stubImage);
            requestCreator.error(stubImage);
        }
        if (!(fadeIn && FADE_ENABLED)) {
            requestCreator.noFade();
        }
        LayoutParams params = imageView.getLayoutParams();
        if (params.width > 0 && params.height > 0) {
            requestCreator.resize(params.width, params.height);
        }
        requestCreator.onlyScaleDown();
        requestCreator.into(imageView, listener);
    }

    public static void displayNoScaling(ImageView imageView, String uri, boolean fadeIn, int stubImage, ImageLoaderListener listener) {
        if (uri == null || uri.length() == 0) {
            uri = FAKE_URI;
        }
        RequestCreator requestCreator = Picasso.get().load(uri);
        if (stubImage != 0) {
            requestCreator.placeholder(stubImage);
            requestCreator.error(stubImage);
        }
        if (!(fadeIn && FADE_ENABLED)) {
            requestCreator.noFade();
        }
        requestCreator.into(imageView, listener);
    }

    public static void loadImage(String uri, FakeImageLoaderListener listener) {
        if (uri == null || uri.length() == 0) {
            uri = FAKE_URI;
        }
        Picasso.get().load(uri).into((Target) listener);
    }

    public static void cancelDisplay(ImageView imageView) {
        if (imageView != null) {
            Picasso.get().cancelRequest(imageView);
        }
    }

    public static void clearImageCache() {
        //Picasso.get().clearCache();
    }
}
