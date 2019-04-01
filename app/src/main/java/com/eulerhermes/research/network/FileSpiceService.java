package com.eulerhermes.research.network;

import android.app.Application;
import com.blandware.android.atleap.provider.ormlite.OrmLiteUriMatcher;
import com.blandware.android.atleap.provider.sqlite.SQLiteUriMatcher;
import com.eulerhermes.research.provider.EHUriMatcher;
import com.octo.android.robospice.SpringAndroidSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.binary.InFileInputStreamObjectPersister;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;
import com.octo.android.robospice.persistence.string.InFileStringObjectPersister;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class FileSpiceService extends SpringAndroidSpiceService {
    private static final int WEBSERVICES_TIMEOUT = 10000;

    public int getThreadCount() {
        return 3;
    }

    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        InFileStringObjectPersister inFileStringObjectPersister = new InFileStringObjectPersister(application);
        InFileInputStreamObjectPersister inFileInputStreamObjectPersister = new InFileInputStreamObjectPersister(application);
        inFileStringObjectPersister.setAsyncSaveEnabled(true);
        inFileInputStreamObjectPersister.setAsyncSaveEnabled(true);
        cacheManager.addPersister(inFileStringObjectPersister);
        cacheManager.addPersister(inFileInputStreamObjectPersister);
        cacheManager.addPersister(new InDatabaseObjectPersisterFactory(application, new RoboSpiceDatabaseHelper(application, "eulerhermes.db", 1), ((OrmLiteUriMatcher) SQLiteUriMatcher.getInstance(EHUriMatcher.class, "com.eulerhermes.research")).getClassToNotificationUriMap()));
        return cacheManager;
    }

    public RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate() {
            protected ClientHttpRequest createRequest(URI url, HttpMethod method) throws IOException {
                ClientHttpRequest request = super.createRequest(url, method);
                request.getHeaders().setAcceptEncoding(ContentCodingType.GZIP);
                return request;
            }
        };
        System.setProperty("http.keepAlive", "false");
        ClientHttpRequestFactory factory = restTemplate.getRequestFactory();
        if (factory instanceof HttpComponentsClientHttpRequestFactory) {
            HttpComponentsClientHttpRequestFactory advancedFactory = (HttpComponentsClientHttpRequestFactory) factory;
            advancedFactory.setConnectTimeout(WEBSERVICES_TIMEOUT);
            advancedFactory.setReadTimeout(WEBSERVICES_TIMEOUT);
        } else if (factory instanceof SimpleClientHttpRequestFactory) {
            SimpleClientHttpRequestFactory advancedFactory2 = (SimpleClientHttpRequestFactory) factory;
            advancedFactory2.setConnectTimeout(WEBSERVICES_TIMEOUT);
            advancedFactory2.setReadTimeout(WEBSERVICES_TIMEOUT);
        }
        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
        List<HttpMessageConverter<?>> listHttpMessageConverters = restTemplate.getMessageConverters();
        listHttpMessageConverters.add(formHttpMessageConverter);
        listHttpMessageConverters.add(stringHttpMessageConverter);
        restTemplate.setMessageConverters(listHttpMessageConverters);
        return restTemplate;
    }
}
