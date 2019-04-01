package com.eulerhermes.research.provider;

import android.net.Uri;
import android.net.Uri.Builder;
import android.provider.BaseColumns;

public class EHSearchContract {
    public static final Uri BASE_CONTENT_URI = new Builder().scheme("content").authority("com.eulerhermes.research").build();
    public static final String CONTENT_AUTHORITY = "com.eulerhermes.research";
    public static final String PATH_SEARCH = "search/#";
    public static final String PATH_SEARCHS = "search";

    public static class Doc implements BaseColumns {
        public static final String CATEGORY = "category";
        public static final Uri CONTENT_URI = EHSearchContract.BASE_CONTENT_URI.buildUpon().appendPath(EHSearchContract.PATH_SEARCHS).build();
        public static final String DESCRIPTION = "description";
        public static final String DOCID = "docId";
        public static final String ISO = "iso";
        public static final String ISSECURED = "isSecured";
        public static final String NAME = "name";
        public static final String PUBDATE = "pubDate";
        public static final String TABLE = "doc";
        public static final String TAGS = "tags";
        public static final String TITLE = "title";
    }

    private EHSearchContract() {
    }
}
