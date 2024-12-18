package com.example.storyappjava.data.remote.response;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class RegisterResponse {

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public void setError(boolean error){
        this.error = error;
    }

    public boolean getError(){
        return error;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public String getMessage(){
        return message;
    }

    @NonNull
    @Override
    public String toString(){
        return
                "RegisterResponse{" +
                        "error = '" + error + '\'' +
                        ",message = '" + message + '\'' +
                        "}";
    }
}
