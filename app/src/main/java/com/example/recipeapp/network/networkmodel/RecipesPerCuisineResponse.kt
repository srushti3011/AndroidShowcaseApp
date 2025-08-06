package com.example.recipeapp.network.networkmodel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RecipesPerCuisineResponse(
    @SerialName("results")
    val results: List<SingleRecipePerCuisine>,
    @SerialName("offset")
    val offset: Int,
    @SerialName("number")
    val number: Int,
    @SerialName("totalResults")
    val totalResults: Int
)