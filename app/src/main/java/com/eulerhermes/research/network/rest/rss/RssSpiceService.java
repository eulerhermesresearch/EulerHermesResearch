package com.eulerhermes.research.network.rest.rss;

import android.app.Application;
import android.util.Log;
import com.google.code.rome.android.repackaged.com.sun.syndication.feed.rss.Channel;
import com.octo.android.robospice.SpringAndroidSpiceService;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.exception.CacheLoadingException;
import com.octo.android.robospice.persistence.exception.CacheSavingException;
import com.octo.android.robospice.persistence.file.InFileObjectPersister;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.feed.RssChannelHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RssSpiceService extends SpringAndroidSpiceService {
    public CacheManager createCacheManager(Application application) throws CacheCreationException {
        CacheManager cacheManager = new CacheManager();
        cacheManager.addPersister(new InFileObjectPersister<Channel>(application, Channel.class) {
            protected Channel readCacheDataFromFile(File file) throws CacheLoadingException {
                return RssSpiceService.getSerializableFromCache(file);
            }

            public Channel saveDataToCacheAndReturnData(Channel Channel, Object cacheKey) throws CacheSavingException {
                RssSpiceService.saveSerializableToCache(Channel, getCacheFile(cacheKey));
                return Channel;
            }
        });
        return cacheManager;
    }

    private static void saveSerializableToCache(Object o, File cacheFile) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.close();
            byte[] buf = bos.toByteArray();
            FileOutputStream fout = new FileOutputStream(cacheFile);
            fout.write(buf);
            fout.close();
        } catch (IOException ioe) {
            Log.e("serializeObject", "error", ioe);
        }
    }

    private static Channel getSerializableFromCache(File file) throws CacheLoadingException {
        if (!file.exists()) {
            return null;
        }
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
            Object object = in.readObject();
            in.close();
            return (Channel) object;
        } catch (ClassNotFoundException cnfe) {
            Log.e("deserializeObject", "class not found error", cnfe);
            throw new CacheLoadingException(cnfe);
        } catch (IOException ioe) {
            Log.e("deserializeObject", "io error", ioe);
            throw new CacheLoadingException(ioe);
        }
    }

    public RestTemplate createRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        RssChannelHttpMessageConverter rssConverter = new RssChannelHttpMessageConverter();
        rssConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.TEXT_XML));
        MappingJacksonHttpMessageConverter jsonConverter = new MappingJacksonHttpMessageConverter();
        List<HttpMessageConverter<?>> listHttpMessageConverters = restTemplate.getMessageConverters();
        listHttpMessageConverters.add(rssConverter);
        listHttpMessageConverters.add(jsonConverter);
        restTemplate.setMessageConverters(listHttpMessageConverters);
        return restTemplate;
    }
}
