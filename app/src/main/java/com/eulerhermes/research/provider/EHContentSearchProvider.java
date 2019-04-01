package com.eulerhermes.research.provider;

import com.blandware.android.atleap.provider.ormlite.OrmLiteProvider;
import com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

public class EHContentSearchProvider extends OrmLiteProvider {
    protected OrmLiteSqliteOpenHelper createHelper() {
        return new EHDatabaseHelper(getContext());
    }

    public EHUriMatcher getUriMatcher() {
        return (EHUriMatcher) SQLiteUriMatcher.getInstance(EHUriMatcher.class, "com.eulerhermes.research");
    }

    public String getAuthority() {
        return "com.eulerhermes.research";
    }
}
