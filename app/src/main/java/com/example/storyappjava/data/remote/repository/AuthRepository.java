package com.example.storyappjava.data.remote.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.response.RegisterResponse;
import com.example.storyappjava.data.remote.retrofit.ApiService;
import com.example.storyappjava.ui.activity.RegisterActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final String TAG = AuthRepository.class.getSimpleName();
    private volatile static AuthRepository INSTANCE = null;

    private final ApiService apiService;

    private final MediatorLiveData<Result<RegisterResponse>> registerResult = new MediatorLiveData<>();

    public AuthRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public static AuthRepository getInstance(ApiService apiService) {
        if (INSTANCE == null) {
            synchronized (AuthRepository.class) {
                INSTANCE = new AuthRepository(apiService);
            }
        }
        return INSTANCE;
    }

    public LiveData<Result<RegisterResponse>> register(String name, String email, String password) {
        registerResult.setValue(new Result.Loading<>());

        Call<RegisterResponse> client = apiService.register(name, email, password);
        client.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        registerResult.setValue(new Result.Success<>(response.body()));
                    }
                } else {
                    registerResult.setValue(new Result.Error<>(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                registerResult.setValue(new Result.Error<>(t.getLocalizedMessage()));
            }
        });
        return registerResult;
    }
}
