package com.example.recipeapp.network.networkservice

import com.example.recipeapp.network.ApiResult
import com.example.recipeapp.network.networkmodel.NewRecipeResponse
import com.example.recipeapp.network.networkmodel.RecipesPerCuisineResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeService {

    @GET("/recipes/complexSearch")
    suspend fun getRecipesPerCuisine(
        @Query("cuisine") cuisine: String
    ): ApiResult<RecipesPerCuisineResponse>

    @GET("/recipes/random")
    suspend fun getNewRecipes(
        @Query("number") number: Int
    ): ApiResult<NewRecipeResponse>
}