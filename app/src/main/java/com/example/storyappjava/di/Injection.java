package com.example.storyappjava.di;

import android.content.Context;

import com.example.storyappjava.data.remote.repository.AuthRepository;
import com.example.storyappjava.data.remote.repository.StoryRepository;
import com.example.storyappjava.data.remote.retrofit.ApiConfig;
import com.example.storyappjava.data.remote.retrofit.ApiService;

public class Injection {
    public static AuthRepository authRepository(Context context) {
        ApiService apiService = ApiConfig.getApiService();
        return AuthRepository.getInstance(apiService);
    }

    public static StoryRepository storyRepository(Context context) {
        ApiService apiService = ApiConfig.getApiService();
        return StoryRepository.getInstance(apiService);
    }
}
