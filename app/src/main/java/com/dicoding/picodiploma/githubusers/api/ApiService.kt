package com.dicoding.picodiploma.githubusers.api

import com.dicoding.picodiploma.githubusers.data.SearchUserResponse
import com.dicoding.picodiploma.githubusers.data.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/users")
    fun getSearchUser(
        @Query("q") id: String
    ): Call<SearchUserResponse>

    @GET("users")
    @Headers("Authorization: token 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")
    fun listUsers(): Call<List<UserResponse>>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun detailUser(
        @Path("username") id: String
    ): Call<UserResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun followersUser(
        @Path("username") id: String
    ): Call<List<UserResponse>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun followingUser(
        @Path("username") id: String
    ): Call<List<UserResponse>>
}