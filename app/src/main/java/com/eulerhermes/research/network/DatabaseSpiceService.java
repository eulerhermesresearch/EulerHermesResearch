/**
 *
 */

package com.eulerhermes.research.network;

import java.util.List;
import android.app.Application;
import com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher;
import com.eulerhermes.research.provider.EHContract;
import com.eulerhermes.research.provider.EHDatabaseHelper;
import com.eulerhermes.research.provider.EHUriMatcher;
import com.octo.android.robospice.SpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;

public class DatabaseSpiceService extends SpiceService
{
    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public CacheManager createCacheManager(Application application) throws CacheCreationException
    {
        CacheManager cacheManager = new CacheManager()
        {

            @Override
            public <T> T loadDataFromCache(Class<T> clazz, Object cacheKey, long maxTimeInCacheBeforeExpiry) throws CacheLoadingException,
                    CacheCreationException
            {
                // TODO Auto-generated method stub
                return (T) new RecentsRequest().loadDataFromNetwork();
            }
        };
        EHUriMatcher matcher = SQLiteUriMatcher.getInstance(EHUriMatcher.class, EHContract.CONTENT_AUTHORITY);

        RoboSpiceDatabaseHelper databaseHelper = new RoboSpiceDatabaseHelper(application, EHDatabaseHelper.DATABASE_NAME,
                                                                             EHDatabaseHelper.DATABASE_VERSION);

        InDatabaseObjectPersisterFactory inDatabaseObjectPersisterFactory = new InDatabaseObjectPersisterFactory(application, databaseHelper,
                                                                                                                 matcher.getClassToNotificationUriMap());

        cacheManager.addPersister(inDatabaseObjectPersisterFactory);
        return cacheManager;
    }
}