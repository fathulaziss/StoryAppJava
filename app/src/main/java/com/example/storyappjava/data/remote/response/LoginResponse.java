package com.example.storyappjava.data.remote.response;

import androidx.annotation.NonNull;

import com.example.storyappjava.data.remote.dto.LoginDto;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("loginResult")
    private LoginDto loginResult;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LoginDto getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(LoginDto loginResult) {
        this.loginResult = loginResult;
    }

    @NonNull
    @Override
    public String toString() {
        return "LoginResponse{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", loginResult=" + loginResult +
                '}';
    }
}
