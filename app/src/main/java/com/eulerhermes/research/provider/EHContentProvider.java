package com.eulerhermes.research.provider;

import com.blandware.android.atleap.provider.ormlite.OrmLiteProvider;
import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;


public class EHContentProvider extends OrmLiteProvider {

    @Override
    protected OrmLiteSqliteOpenHelper createHelper() {
        return new EHDatabaseHelper(getContext());
    }

    @Override
    public EHUriMatcher getUriMatcher() {
        return OrmLiteUriMatcher.getInstance(EHUriMatcher.class, EHContract.CONTENT_AUTHORITY);
    }

    @Override
    public String getAuthority() {
        return EHContract.CONTENT_AUTHORITY;
    }
}