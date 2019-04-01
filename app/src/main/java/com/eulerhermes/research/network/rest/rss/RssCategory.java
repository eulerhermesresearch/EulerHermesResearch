package com.eulerhermes.research.network.rest.rss;

import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Category;

public final class RssCategory {
    public static final String APP = "APP";
    public static final String PDF = "PDF";
    public static final String WEB = "WEB";

    public static boolean isApp(Category category) {
        return category != null && category.getValue().equals(APP);
    }

    public static boolean isPdf(Category category) {
        return category != null && category.getValue().equals(PDF);
    }

    public static boolean isWeb(Category category) {
        return category != null && category.getValue().equals(WEB);
    }
}
