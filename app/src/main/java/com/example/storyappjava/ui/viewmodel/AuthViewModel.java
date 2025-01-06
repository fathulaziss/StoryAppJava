package com.example.storyappjava.ui.viewmodel;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.storyappjava.data.remote.Result;
import com.example.storyappjava.data.remote.repository.AuthRepository;
import com.example.storyappjava.data.remote.response.LoginResponse;
import com.example.storyappjava.data.remote.response.RegisterResponse;

public class AuthViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<Result<RegisterResponse>> registerResult = new MutableLiveData<>();
    private final MutableLiveData<Result<LoginResponse>> loginResult = new MutableLiveData<>();

    public AuthViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<Result<RegisterResponse>> getRegisterResult() {
        return registerResult;
    }

    public LiveData<Result<LoginResponse>> getLoginResult() {
        return loginResult;
    }

    public void register(LifecycleOwner lifecycleOwner, String name, String email, String password) {
        authRepository.register(name, email, password).observe(lifecycleOwner, registerResult::postValue);
    }

    public void login(LifecycleOwner lifecycleOwner, String email, String password) {
        authRepository.login(email, password).observe(lifecycleOwner, loginResult::postValue);
    }
}
