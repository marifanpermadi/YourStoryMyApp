package com.example.yourstorymyapp.data.api

import com.example.yourstorymyapp.data.model.ApiResponse
import com.example.yourstorymyapp.data.model.LoginResponse
import com.example.yourstorymyapp.data.model.RegisterResponse
import com.example.yourstorymyapp.data.model.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") pass: String
    ): Call<LoginResponse>

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("location") location: Int?= null
    ): StoryResponse

    @Multipart
    @POST("stories")
    fun addStories(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float ?= null,
        @Part("lon") lon: Float ?= null
    ): Call<ApiResponse>

    @GET("stories?location=1")
    fun getStoriesWithLocation(
        @Header("Authorization") header: String,
        @Query("location") location: Int
    ) : Call<StoryResponse>

}