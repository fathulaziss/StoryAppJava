package com.example.storyappjava.ui.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.repository.StoryRepository;
import com.example.storyappjava.data.remote.response.LoginResponse;
import com.example.storyappjava.data.remote.response.RegisterResponse;
import com.example.storyappjava.data.remote.response.StoryResponse;

import java.io.File;

public class StoryViewModel extends ViewModel {
    private final StoryRepository storyRepository;
    private final MutableLiveData<Result<StoryResponse>> getStoryResult = new MutableLiveData<>();
    private final MutableLiveData<Result<StoryResponse>> uploadStoryResult = new MutableLiveData<>();
    private final MutableLiveData<Result<StoryResponse>> getDetailStoryResult = new MutableLiveData<>();

    public StoryViewModel(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public LiveData<Result<StoryResponse>> getStoryResult() {
        return getStoryResult;
    }

    public LiveData<Result<StoryResponse>> getDetailStoryResult() {
        return getDetailStoryResult;
    }

    public LiveData<Result<StoryResponse>> getUploadStoryResult() {
        return uploadStoryResult;
    }

    public void getStories(LifecycleOwner lifecycleOwner, String token, Integer page, Integer size, Integer location) {
        storyRepository.getStories(token, page, size, location).observe(lifecycleOwner, getStoryResult::postValue);
    }

    public void getDetailStory(LifecycleOwner lifecycleOwner, String token, String id) {
        storyRepository.getDetailStory(token, id).observe(lifecycleOwner, getDetailStoryResult::postValue);
    }

    public void uploadStory(LifecycleOwner lifecycleOwner, String token, String description, File photo, Float lat, Float lon) {
        storyRepository.uploadStory(token, description, photo, lat, lon).observe(lifecycleOwner, uploadStoryResult::postValue);
    }
}