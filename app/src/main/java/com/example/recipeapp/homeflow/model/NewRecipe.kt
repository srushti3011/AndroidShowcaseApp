package com.example.recipeapp.homeflow.model

data class NewRecipe(
    val id: Int,
    val image: String,
    val title: String,
    val readyInMinutes: Int,
    val sourceName: String,
    var isLoading: Boolean
)
