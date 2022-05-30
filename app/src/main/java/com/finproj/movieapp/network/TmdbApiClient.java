package com.finproj.movieapp.network;

import com.finproj.movieapp.helper.DataHolder;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TmdbApiClient {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY = "beebd7135aa2fe7696c65be4afbdde74";
    String id = null;

    public TmdbApiInterface apiInterface() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).client(okHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(TmdbApiInterface.class);
    }

    public OkHttpClient okHttpClient() {
        id = DataHolder.getInstance().getData();

        return new OkHttpClient.Builder().addInterceptor(chain -> {
            Request original = chain.request();
            HttpUrl url;
            if (id != null) {
                url = original.url().newBuilder()
                        .addPathSegments(id)
                        .addQueryParameter("api_key", API_KEY)
                        .build();
            }
            else {
                url = original.url().newBuilder()
                        .addQueryParameter("api_key", API_KEY)
                        .build();
            }

            Request.Builder builder = original.newBuilder().url(url);
            return chain.proceed(builder.build());
        }).build();
    }
}
