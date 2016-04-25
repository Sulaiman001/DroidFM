package com.stafiiyevskyi.mlsdev.droidfm.data.api;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by oleksandr on 20.04.16.
 */
public class LastFMRestClient {

    private static LastFMService service;

    private static final String BASE_URL = "http://ws.audioscrobbler.com/2.0/";


    private static Map<String, String> query;

    private LastFMRestClient() {
    }

    public static Map<String, String> getAdditionalQuery() {
        if (query != null) {
            return query;
        } else {
            query = new LinkedHashMap<>();
            query.put("format", "json");
            query.put("api_key", "c0cca0938e628d1582474f036955fcfa");
            return query;
        }

    }


    public static LastFMService getService() {
        if (service == null) {
            Interceptor interceptor = chain -> {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("format", "json")
                        .addQueryParameter("api_key", "c0cca0938e628d1582474f036955fcfa").build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            };
            HttpLoggingInterceptor interceptorLogging = new HttpLoggingInterceptor();
            interceptorLogging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.interceptors().add(interceptorLogging);
            builder.interceptors().add(interceptor);
            OkHttpClient client = builder.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(LastFMService.class);
            return service;
        } else {
            return service;
        }

    }
}
