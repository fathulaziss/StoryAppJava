package com.example.storyappjava.data.remote.retrofit;

import com.example.storyappjava.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiConfig {
    public static ApiService getApiService() {
        HttpLoggingInterceptor loggingInterceptor;

        if (BuildConfig.DEBUG) {
            loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(ApiService.class);
    }
}
