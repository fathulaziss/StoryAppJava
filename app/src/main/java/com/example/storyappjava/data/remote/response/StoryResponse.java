package com.example.storyappjava.data.remote.response;

import androidx.annotation.NonNull;

import com.example.storyappjava.data.remote.dto.StoryDto;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoryResponse {

    @SerializedName("error")
    private Boolean error;

    @SerializedName("message")
    private String message;

    @SerializedName("listStory")
    private List<StoryDto> listStory;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<StoryDto> getListStory() {
        return listStory;
    }

    public void setListStory(List<StoryDto> listStory) {
        this.listStory = listStory;
    }

    @NonNull
    @Override
    public String toString() {
        return "StoryResponse{" +
                "error=" + error +
                ", message='" + message + '\'' +
                ", listStory=" + listStory +
                '}';
    }
}
