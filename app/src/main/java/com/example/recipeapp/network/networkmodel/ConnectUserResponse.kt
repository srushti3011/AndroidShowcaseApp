package com.example.recipeapp.network.networkmodel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ConnectUserResponse(
    @SerialName("status")
    val status: String,
    @SerialName("username")
    val userName: String,
    @SerialName("spoonacularPassword")
    val spoonacularPassword: String,
    @SerialName("hash")
    val hash: String
)