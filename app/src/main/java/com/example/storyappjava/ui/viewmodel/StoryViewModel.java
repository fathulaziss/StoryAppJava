package com.example.storyappjava.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.repository.StoryRepository;
import com.example.storyappjava.data.remote.response.StoryResponse;

public class StoryViewModel extends ViewModel {
    private final StoryRepository storyRepository;

    public StoryViewModel(StoryRepository storyRepository) {
        this.storyRepository = storyRepository;
    }

    public LiveData<Result<StoryResponse>> getStories(String token, Integer page, Integer size, Integer location) {
       return storyRepository.getStories(token, page, size, location);
    }
}
