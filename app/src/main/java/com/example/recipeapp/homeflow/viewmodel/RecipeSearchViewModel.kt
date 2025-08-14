package com.example.recipeapp.homeflow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.network.ApiState
import com.example.recipeapp.network.ErrorState
import com.example.recipeapp.network.Loading
import com.example.recipeapp.network.Success
import com.example.recipeapp.network.networkmodel.ComplexQueryResponseSingleRecipe
import com.example.recipeapp.network.networkmodel.ComplexRecipeQueryResponse
import com.example.recipeapp.network.networkrepository.RecipeRepository
import com.example.recipeapp.network.onError
import com.example.recipeapp.network.onException
import com.example.recipeapp.network.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class RecipeSearchViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
): ViewModel() {

    private var previousSearchRecipeJob: Job? = null

    private var mRecentSearchRecipes = listOf(
        ComplexQueryResponseSingleRecipe(
            id = 715415,
            title = "Red Lentil Soup with Chicken and Turnips",
            image = "https://img.spoonacular.com/recipes/715415-312x231.jpg",
            imageType = "jpg"
        ),
        ComplexQueryResponseSingleRecipe(
            id = 714606,
            title = "Asparagus and Pea Soup: Real Convenience Food",
            image = "https://img.spoonacular.com/recipes/716406-312x231.jpg",
            imageType = "jpg"
        ),
        ComplexQueryResponseSingleRecipe(
            id = 644387,
            title = "Garlicky Kale",
            image = "https://img.spoonacular.com/recipes/644387-312x231.jpg",
            imageType = "jpg"
        ),
        ComplexQueryResponseSingleRecipe(
            id = 715446,
            title = "Slow Cooker Beef Stew",
            image = "https://img.spoonacular.com/recipes/715446-312x231.jpg",
            imageType = "jpg"
        ),
        ComplexQueryResponseSingleRecipe(
            id = 782601,
            title = "Red Kidney Bean Jambalaya",
            image = "https://img.spoonacular.com/recipes/782601-312x231.jpg",
            imageType = "jpg"
        ),
        ComplexQueryResponseSingleRecipe(
            id = 716426,
            title = "Cauliflower, Brown Rice, and Vegetable Fried Rice",
            image = "https://img.spoonacular.com/recipes/716426-312x231.jpg",
            imageType = "jpg"
        ),
        ComplexQueryResponseSingleRecipe(
            id = 716004,
            title = "Quinoa and Chickpea Salad with Sun-Dried Tomatoes and Dried Cherries",
            image = "https://img.spoonacular.com/recipes/716004-312x231.jpg",
            imageType = "jpg"
        ),
        ComplexQueryResponseSingleRecipe(
            id = 716627,
            title = "Easy Homemade Rice and Beans",
            image = "https://img.spoonacular.com/recipes/716627-312x231.jpg",
            imageType = "jpg"
        ),
        ComplexQueryResponseSingleRecipe(
            id = 664147,
            title = "Tuscan White Bean Soup with Olive Oil and Rosemary",
            image = "https://img.spoonacular.com/recipes/664147-312x231.jpg",
            imageType = "jpg"
        ),
        ComplexQueryResponseSingleRecipe(
            id = 640941,
            title = "Crunchy Brussels Sprouts Side Dish",
            image = "https://img.spoonacular.com/recipes/640941-312x231.jpg",
            imageType = "jpg"
        )
    )
    val recentSearchRecipes: List<ComplexQueryResponseSingleRecipe>
        get() = mRecentSearchRecipes

    private var mSearchRecipeApiState = MutableLiveData<ApiState<ComplexRecipeQueryResponse>>()
    val searchRecipeApiState: LiveData<ApiState<ComplexRecipeQueryResponse>>
        get() = mSearchRecipeApiState

    fun getRecipesFor(query: String) {
        cancelJob()
        previousSearchRecipeJob = viewModelScope.launch {
            delay(500)
            mSearchRecipeApiState.value = Loading()
            recipeRepository.searchRecipesFor(query)
                .onSuccess {
                    mSearchRecipeApiState.value = Success(it)
                }
                .onException {
                    it.localizedMessage?.let { it1 -> Log.i("TAG", it1) }
                }
                .onError { code, message ->
                    mSearchRecipeApiState.value = ErrorState(message)
                }
        }
    }

    private fun cancelJob() {
        previousSearchRecipeJob?.cancel()
    }
}