package com.example.taskn18.users.api

import com.example.taskn18.users.model.UserDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersApi {
    @GET("users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): UserDataResponse
}