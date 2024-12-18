package com.example.storyappjava.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.repository.AuthRepository;
import com.example.storyappjava.data.remote.response.RegisterResponse;

public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository;

    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Result<RegisterResponse>> register(String name, String email, String password) {
        return authRepository.register(name, email, password);
    }
}
