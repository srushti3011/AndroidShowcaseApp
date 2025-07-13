package com.example.recipeapp.network.networkrepository

import com.example.recipeapp.network.ApiResult
import com.example.recipeapp.network.networkmodel.ConnectUserBody
import com.example.recipeapp.network.networkmodel.ConnectUserResponse
import com.example.recipeapp.network.networkservice.UserService
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userService: UserService
): UserRespository {

    override suspend fun connectUser(body: ConnectUserBody): ApiResult<ConnectUserResponse> {
        return userService.connectUser(body)
    }
}