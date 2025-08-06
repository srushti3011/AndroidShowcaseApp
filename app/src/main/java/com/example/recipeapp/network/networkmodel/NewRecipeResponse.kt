package com.example.recipeapp.network.networkmodel


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewRecipeResponse(
    @SerialName("recipes")
    val singleNewRecipes: List<SingleNewRecipe>
)