package com.example.recipeapp.network.networkservice

import com.example.recipeapp.network.ApiResult
import com.example.recipeapp.network.networkmodel.ConnectUserBody
import com.example.recipeapp.network.networkmodel.ConnectUserResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

    @POST("users/connect")
    suspend fun connectUser(
        @Body userDetails: ConnectUserBody
    ): ApiResult<ConnectUserResponse>
}