package com.example.recipeapp.network.networkmodel

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ComplexRecipeQueryResponse(
    @SerialName("results")
    val results: List<ComplexQueryResponseSingleRecipe>,
    @SerialName("offset")
    val offset: Int,
    @SerialName("number")
    val number: Int,
    @SerialName("totalResults")
    val totalResults: Int
)