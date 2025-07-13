package com.example.recipeapp.network.networkrepository

import com.example.recipeapp.network.ApiResult
import com.example.recipeapp.network.networkmodel.ConnectUserBody
import com.example.recipeapp.network.networkmodel.ConnectUserResponse

interface UserRespository {
    suspend fun connectUser(body: ConnectUserBody): ApiResult<ConnectUserResponse>
}