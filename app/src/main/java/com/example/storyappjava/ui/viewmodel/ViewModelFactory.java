package com.example.storyappjava.ui.viewmodel;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.storyappjava.data.remote.repository.AuthRepository;
import com.example.storyappjava.data.remote.repository.StoryRepository;
import com.example.storyappjava.di.Injection;

public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static volatile ViewModelFactory INSTANCE;

    private final AuthRepository authRepository;
    private final StoryRepository storyRepository;

    private ViewModelFactory(
            AuthRepository authRepository,
            StoryRepository storyRepository
    ) {
        this.authRepository = authRepository;
        this.storyRepository = storyRepository;
    }

    public static ViewModelFactory getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (ViewModelFactory.class) {
                INSTANCE = new ViewModelFactory(
                        Injection.authRepository(context),
                        Injection.storyRepository(context)
                        );
            }
        }
        return INSTANCE;
    }

    /** @noinspection unchecked*/
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AuthViewModel.class)) {
            return (T) new AuthViewModel(authRepository);
        } else if (modelClass.isAssignableFrom(StoryViewModel.class)) {
            return (T) new  StoryViewModel(storyRepository);
        }

        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
