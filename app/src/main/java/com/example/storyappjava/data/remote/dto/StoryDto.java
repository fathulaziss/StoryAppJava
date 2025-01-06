package com.example.storyappjava.data.remote.dto;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class StoryDto {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("photoUrl")
    private String photoUrl;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("lat")
    private Double lat;

    @SerializedName("lon")
    private Double lon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    @NonNull
    @Override
    public String toString() {
        return "StoryDto{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
