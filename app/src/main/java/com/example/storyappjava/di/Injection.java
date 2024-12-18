package com.example.storyappjava.di;

import android.content.Context;

import com.example.storyappjava.data.remote.repository.AuthRepository;
import com.example.storyappjava.data.remote.retrofit.ApiConfig;
import com.example.storyappjava.data.remote.retrofit.ApiService;

public class Injection {
    public static AuthRepository provideRepository(Context context) {
        ApiService apiService = ApiConfig.getApiService();
        return AuthRepository.getInstance(apiService);
    }
}
