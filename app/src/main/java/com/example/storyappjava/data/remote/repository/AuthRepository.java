package com.example.storyappjava.data.remote.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.response.LoginResponse;
import com.example.storyappjava.data.remote.response.RegisterResponse;
import com.example.storyappjava.data.remote.retrofit.ApiService;
import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepository {
    private final String TAG = AuthRepository.class.getSimpleName();
    private volatile static AuthRepository INSTANCE = null;

    private final ApiService apiService;

    private final MediatorLiveData<Result<RegisterResponse>> registerResult = new MediatorLiveData<>();
    private final MediatorLiveData<Result<LoginResponse>> loginResult = new MediatorLiveData<>();

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
                Log.d(TAG, "response code : " + response.code());
                Log.d(TAG, "response message : " + response.message());
                if (response.isSuccessful()) {
                    Log.d(TAG, "response body : " + response.body());
                    if (response.body() != null) {
                        registerResult.setValue(new Result.Success<>(response.body()));
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.d(TAG, "response error body : " + errorBody);

                            Gson gson = new Gson();
                            RegisterResponse errorResponse = gson.fromJson(errorBody, RegisterResponse.class);
                            registerResult.setValue(new Result.Error<>(errorResponse.getMessage()));
                        }
                    } catch (IOException e) {
                        Log.e(TAG,"Error reading the response body", e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                registerResult.setValue(new Result.Error<>(t.getLocalizedMessage()));
            }
        });
        return registerResult;
    }

    public LiveData<Result<LoginResponse>> login(String email, String password) {
        loginResult.setValue(new Result.Loading<>());

        Call<LoginResponse> client = apiService.login(email, password);
        client.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                Log.d(TAG, "response code : " + response.code());
                Log.d(TAG, "response message : " + response.message());
                if (response.isSuccessful()) {
                    Log.d(TAG, "response body : " + response.body());
                    if (response.body() != null) {
                        loginResult.setValue(new Result.Success<>(response.body()));
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.d(TAG, "response error body : " + errorBody);

                            Gson gson = new Gson();
                            LoginResponse errorResponse = gson.fromJson(errorBody, LoginResponse.class);
                            loginResult.setValue(new Result.Error<>(errorResponse.getMessage()));
                        }
                    } catch (IOException e) {
                        Log.e(TAG,"Error reading the response body", e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                loginResult.setValue(new Result.Error<>(t.getLocalizedMessage()));
            }
        });
        return loginResult;
    }
}
