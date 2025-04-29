package com.example.storyappjava.data.remote.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.response.StoryResponse;
import com.example.storyappjava.data.remote.retrofit.ApiService;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryRepository {
    private final String TAG = StoryRepository.class.getSimpleName();
    private volatile static StoryRepository INSTANCE = null;

    private final ApiService apiService;

    private final MediatorLiveData<Result<StoryResponse>> storiesResult = new MediatorLiveData<>();
    private final MediatorLiveData<Result<StoryResponse>> uploadResult = new MediatorLiveData<>();
    private final MediatorLiveData<Result<StoryResponse>> storyDetailResult = new MediatorLiveData<>();

    public StoryRepository(ApiService apiService) {
        this.apiService = apiService;
    }

    public static StoryRepository getInstance(ApiService apiService) {
        if (INSTANCE == null) {
            synchronized (StoryRepository.class) {
                INSTANCE = new StoryRepository(apiService);
            }
        }
        return INSTANCE;
    }

    public LiveData<Result<StoryResponse>> getStories(
            String token,
            Integer page,
            Integer size,
            Integer location
    ) {
        storiesResult.setValue(new Result.Loading<>());

        Call<StoryResponse> client = apiService.getStories(token, page, size, location);
        client.enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                Log.d(TAG, "response code : " + response.code());
                Log.d(TAG, "response message : " + response.message());
                if (response.isSuccessful()) {
                    Log.d(TAG, "response body : " + response.body());
                    if (response.body() != null) {
                        storiesResult.setValue(new Result.Success<>(response.body()));
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.d(TAG, "response error body : " + errorBody);

                            Gson gson = new Gson();
                            StoryResponse errorResponse = gson.fromJson(errorBody, StoryResponse.class);
                            storiesResult.setValue(new Result.Error<>(errorResponse.getMessage()));
                        }
                    } catch (IOException e) {
                        Log.e(TAG,"Error reading the response body", e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                storiesResult.setValue(new Result.Error<>(t.getLocalizedMessage()));
            }
        });
        return storiesResult;
    }

    public LiveData<Result<StoryResponse>> getDetailStory(String token, String id) {
        storyDetailResult.setValue(new Result.Loading<>());

        Call<StoryResponse> client = apiService.getStoryDetail(token, id);
        client.enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                Log.d(TAG, "response code : " + response.code());
                Log.d(TAG, "response message : " + response.message());
                if (response.isSuccessful()) {
                    Log.d(TAG, "response body : " + response.body());
                    if (response.body() != null) {
                        storyDetailResult.setValue(new Result.Success<>(response.body()));
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.d(TAG, "response error body : " + errorBody);

                            Gson gson = new Gson();
                            StoryResponse errorResponse = gson.fromJson(errorBody, StoryResponse.class);
                            storyDetailResult.setValue(new Result.Error<>(errorResponse.getMessage()));
                        }
                    } catch (IOException e) {
                        Log.e(TAG,"Error reading the response body", e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                storyDetailResult.setValue(new Result.Error<>(t.getLocalizedMessage()));
            }
        });
        return storyDetailResult;
    }

    public LiveData<Result<StoryResponse>> uploadStory(String token, String description, File photo, Float lat, Float lon) {
        uploadResult.setValue(new Result.Loading<>());

        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody latBody = lat != null ? RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lat)) : null;
        RequestBody lonBody = lon != null ? RequestBody.create(MediaType.parse("text/plain"), String.valueOf(lon)) : null;

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), photo);
        MultipartBody.Part photoPart = MultipartBody.Part.createFormData("photo", photo.getName(), requestFile);

        Call<StoryResponse> client = apiService.uploadStory(token, descriptionBody, photoPart, latBody, lonBody);
        client.enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(@NonNull Call<StoryResponse> call, @NonNull Response<StoryResponse> response) {
                Log.d(TAG, "response code : " + response.code());
                Log.d(TAG, "response message : " + response.message());
                if (response.isSuccessful()) {
                    Log.d(TAG, "response body : " + response.body());
                    if (response.body() != null) {
                        uploadResult.setValue(new Result.Success<>(response.body()));
                    }
                } else {
                    try {
                        if (response.errorBody() != null) {
                            String errorBody = response.errorBody().string();
                            Log.d(TAG, "response error body : " + errorBody);

                            Gson gson = new Gson();
                            StoryResponse errorResponse = gson.fromJson(errorBody, StoryResponse.class);
                            uploadResult.setValue(new Result.Error<>(errorResponse.getMessage()));
                        }
                    } catch (IOException e) {
                        Log.e(TAG,"Error reading the response body", e);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<StoryResponse> call, @NonNull Throwable t) {
                uploadResult.setValue(new Result.Error<>(t.getLocalizedMessage()));
            }
        });
        return uploadResult;
    }
}