package com.example.storyappjava.data.remote.retrofit;

import com.example.storyappjava.data.remote.response.LoginResponse;
import com.example.storyappjava.data.remote.response.RegisterResponse;
import com.example.storyappjava.data.remote.response.StoryResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @FormUrlEncoded
    @POST("register")
    Call<RegisterResponse> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    @GET("stories")
    Call<StoryResponse> getStories(
            @Header("Authorization") String token,
            @Query("page") int page,
            @Query("size") int size,
            @Query("location") int location
    );

    @GET("stories/{id}")
    Call<StoryResponse> getStoryDetail(
            @Header("Authorization") String token,
            @Path("id") String storyId
    );

    @Multipart
    @POST("stories")
    Call<StoryResponse> uploadStory(
            @Header("Authorization") String token,
            @Part("description") RequestBody description,
            @Part MultipartBody.Part photo,
            @Part("lat") RequestBody lat,
            @Part("lon") RequestBody lon
    );
}