package com.eulerhermes.research.provider;

import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.eulerhermes.research.model.Doc;

public class EHSearchUriMatcher extends OrmLiteUriMatcher {
    public EHSearchUriMatcher(String authority) {
        super(authority);
    }

    public void instantiate() {
        addClass(EHSearchContract.PATH_SEARCHS, Doc.class);
        addClass(EHSearchContract.PATH_SEARCH, Doc.class);
    }
}
