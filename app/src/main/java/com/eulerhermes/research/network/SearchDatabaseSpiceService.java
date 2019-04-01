package com.eulerhermes.research.network;

import android.app.Application;
import com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher;
import com.eulerhermes.research.provider.EHSearchUriMatcher;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;

public class SearchDatabaseSpiceService extends SpiceService {
    public void onCreate() {
        super.onCreate();
    }

    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        cacheManager.addPersister(new InDatabaseObjectPersisterFactory(application, new RoboSpiceDatabaseHelper(application, "eulerhermes.db", 1), ((EHSearchUriMatcher) SQLiteUriMatcher.getInstance(EHSearchUriMatcher.class, "com.eulerhermes.research")).getClassToNotificationUriMap()));
        return cacheManager;
    }
}
