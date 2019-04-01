package com.eulerhermes.research.provider;

import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.eulerhermes.research.model.Doc;

public class EHUriMatcher extends OrmLiteUriMatcher {
    public EHUriMatcher(String authority) {
        super(authority);
    }

    public void instantiate() {
        addClass(EHContract.PATH_RECENTS, Doc.class);
        addClass(EHContract.PATH_RECENT, Doc.class);
    }
}
