package com.eulerhermes.research.network.rest;

import com.eulerhermes.research.BuildConfig;
import com.eulerhermes.research.network.EHRetryPolicy;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.octo.android.robospice.request.googlehttpclient.GoogleHttpClientSpiceRequest;
import java.io.IOException;
import org.json.JSONObject;
import org.springframework.http.MediaType;

public abstract class BaseRequest<T> extends GoogleHttpClientSpiceRequest<T> {
    protected static final int DELETE = 3;
    protected static final int GET = 0;
    protected static final int POST = 1;
    protected static final int PUT = 2;

    protected abstract String getURL();

    public BaseRequest(Class<T> clazz) {
        super(clazz);
        setRetryPolicy(new EHRetryPolicy());
    }

    public T loadDataFromNetwork() throws IOException {
        HttpRequest request;
        GenericUrl url = new GenericUrl(getURL());
        switch (getRequestType()) {
            case 1:
                request = getHttpRequestFactory().buildPostRequest(url, getContent());
                break;
            case 2:
                request = getHttpRequestFactory().buildPutRequest(url, getContent());
                break;
            case 3:
                request = getHttpRequestFactory().buildDeleteRequest(url);
                break;
            default:
                request = getHttpRequestFactory().buildGetRequest(url);
                break;
        }
        request.setParser(new JacksonFactory().createJsonObjectParser());
        HttpHeaders headers = new HttpHeaders();
        headers.setAuthorization(getAuthorization());
        headers.setContentType(MediaType.APPLICATION_JSON_VALUE);
        request.setHeaders(headers);
        return request.execute().parseAs(getResultType());
    }

    protected String getAuthorization() {
        return BuildConfig.BASIC_AUTH_1;
    }

    protected int getRequestType() {
        return 0;
    }

    protected HttpContent getContent() {
        return null;
    }

    protected HttpContent contentFromString(String json) {
        return ByteArrayContent.fromString(MediaType.APPLICATION_JSON_VALUE, json);
    }

    protected HttpContent contentFromJson(JSONObject jsonObject) {
        return new JsonHttpContent(new JacksonFactory(), jsonObject);
    }
}
