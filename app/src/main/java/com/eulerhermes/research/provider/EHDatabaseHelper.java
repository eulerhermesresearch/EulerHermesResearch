package com.eulerhermes.research.provider;

import android.content.Context;
import com.blandware.android.atleap.provider.ormlite.OrmLiteDatabaseHelper;
import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher;

public class EHDatabaseHelper extends OrmLiteDatabaseHelper {
    public static final String DATABASE_NAME = "eulerhermes.db";
    public static final int DATABASE_VERSION = 1;

    public EHDatabaseHelper(Context context) {
        super(context, "eulerhermes.db", 1);
    }

    public OrmLiteUriMatcher getUriMatcher() {
        return (OrmLiteUriMatcher) SQLiteUriMatcher.getInstance(EHUriMatcher.class, "com.eulerhermes.research");
    }
}
