package com.example.recipeapp.network.networkrepository

import com.example.recipeapp.network.ApiResult
import com.example.recipeapp.network.networkmodel.ComplexRecipeQueryResponse
import com.example.recipeapp.network.networkmodel.NewRecipeResponse
import com.example.recipeapp.network.networkmodel.RecipesPerCuisineResponse
import com.example.recipeapp.network.networkservice.RecipeService
import jakarta.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val recipeService: RecipeService
): RecipeRepository {

    override suspend fun getRecipesPerCuisine(
        cuisine: String
    ): ApiResult<RecipesPerCuisineResponse> {
        return recipeService.getRecipesPerCuisine(cuisine)
    }

    override suspend fun getNewRecipes(number: Int): ApiResult<NewRecipeResponse> {
        return recipeService.getNewRecipes(number)
    }

    override suspend fun searchRecipesFor(
        query: String,
        diet: List<String>?,
        type: List<String>?
    ): ApiResult<ComplexRecipeQueryResponse> {
        return recipeService.searchRecipesFor(query, diet, type)
    }
}