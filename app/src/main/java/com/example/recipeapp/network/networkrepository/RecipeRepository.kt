package com.example.recipeapp.network.networkrepository

import com.example.recipeapp.network.ApiResult
import com.example.recipeapp.network.networkmodel.NewRecipeResponse
import com.example.recipeapp.network.networkmodel.RecipesPerCuisineResponse

interface RecipeRepository {
    suspend fun getRecipesPerCuisine(cuisine: String): ApiResult<RecipesPerCuisineResponse>
    suspend fun getNewRecipes(number: Int): ApiResult<NewRecipeResponse>
}