package com.example.storyappjava.ui.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.repository.AuthRepository;
import com.example.storyappjava.data.remote.response.RegisterResponse;
import com.example.storyappjava.ui.activity.LoginActivity;

public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<Result<RegisterResponse>> registerResult = new MutableLiveData<>();

    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Result<RegisterResponse>> getRegisterResult() {
        return registerResult;
    }

    public void register(LifecycleOwner lifecycleOwner, String name, String email, String password) {
        authRepository.register(name, email, password).observe(lifecycleOwner, registerResult::postValue);
    }
}
