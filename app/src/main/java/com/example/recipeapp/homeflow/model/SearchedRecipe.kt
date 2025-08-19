package com.example.recipeapp.homeflow.model

data class SearchedRecipe (
    val id: Int,
    val title: String,
    val image: String,
    val isLoading: Boolean = false
)