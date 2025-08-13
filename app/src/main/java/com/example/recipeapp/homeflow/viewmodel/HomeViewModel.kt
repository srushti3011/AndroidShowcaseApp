package com.example.recipeapp.homeflow.viewmodel

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.homeflow.model.Cuisine
import com.example.recipeapp.homeflow.model.NewRecipe
import com.example.recipeapp.homeflow.model.RecipePerCuisine
import com.example.recipeapp.navigation.route.HomeRoute
import com.example.recipeapp.network.ApiState
import com.example.recipeapp.network.ErrorState
import com.example.recipeapp.network.Loading
import com.example.recipeapp.network.Success
import com.example.recipeapp.network.networkmodel.NewRecipeResponse
import com.example.recipeapp.network.networkmodel.RecipesPerCuisineResponse
import com.example.recipeapp.network.networkmodel.SingleNewRecipe
import com.example.recipeapp.network.networkmodel.SingleRecipePerCuisine
import com.example.recipeapp.network.networkrepository.RecipeRepository
import com.example.recipeapp.network.onError
import com.example.recipeapp.network.onException
import com.example.recipeapp.network.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val homeRoute: HomeRoute
) : ViewModel() {

    private var mCuisines = arrayOf(
        Cuisine(
            name = "Indian",
            isSelected = true
        ),
        Cuisine(
            name = "Italian",
            isSelected = false
        ),
        Cuisine(
            name = "Japanese",
            isSelected = false
        ),
        Cuisine(
            name = "Thai",
            isSelected = false
        ),
        Cuisine(
            name = "Mexican",
            isSelected = false
        )
    )
    val cuisines: Array<Cuisine>
        get() = mCuisines

    private var mRecipesPerCuisineApiState = MutableLiveData<ApiState<RecipesPerCuisineResponse>>()
    val recipePerCuisineApiState: LiveData<ApiState<RecipesPerCuisineResponse>>
        get() = mRecipesPerCuisineApiState

    private var mRecipesPerCuisineData = MutableLiveData<List<RecipePerCuisine>>()
    val recipePerCuisineData: LiveData<List<RecipePerCuisine>>
        get() = mRecipesPerCuisineData

    private var mNewRecipesApiState = MutableLiveData<ApiState<NewRecipeResponse>>()
    val newRecipeApiState: LiveData<ApiState<NewRecipeResponse>>
        get() = mNewRecipesApiState

    private var mNewRecipeData = MutableLiveData<List<NewRecipe>>()
    val newRecipeData: LiveData<List<NewRecipe>>
        get() = mNewRecipeData

    fun convertCuisinesToInitialState() {
        mCuisines.forEachIndexed { ind, value ->
            if (ind == 0) {
                value.isSelected = true
            } else {
                value.isSelected = false
            }
        }
    }

    fun recipeCuisineSet(cuisine: String) {
        mRecipesPerCuisineApiState.value = Loading()
        viewModelScope.launch {
            recipeRepository.getRecipesPerCuisine(cuisine)
                .onSuccess {
                    Log.i("TAG", it.toString())
                    convertRecipePerCuisineResponseToUIModel(it.results)
                    mRecipesPerCuisineApiState.value = Success(it)
                }
                .onError { code, message ->
                    Log.i("TAG", "$code returned with $message")
                    mRecipesPerCuisineApiState.value = ErrorState(message)
                }
                .onException {
                    it.localizedMessage?.let { it1 -> Log.i("TAG", it1) }
                }
        }
    }

    private fun convertRecipePerCuisineResponseToUIModel(
        recipes: List<SingleRecipePerCuisine>
    ) {
        val recipePerCuisineUIList = mutableListOf<RecipePerCuisine>()
         recipes.forEach {
             val recipePerCuisineToBeAdded = RecipePerCuisine(
                 id = it.id,
                 title = it.title,
                 image = it.image,
                 isLoading = false
             )
             recipePerCuisineUIList.add(recipePerCuisineToBeAdded)
         }
        mRecipesPerCuisineData.value = recipePerCuisineUIList
    }

    fun getNewRecipes(number: Int) {
        mNewRecipesApiState.value = Loading()
        viewModelScope.launch {
            recipeRepository.getNewRecipes(number)
                .onSuccess {
                    Log.i("TAG", it.toString())
                    convertNewRecipeResponseToUIModel(it.singleNewRecipes)
                    mNewRecipesApiState.value = Success(it)
                }
                .onError { code, message ->
                    Log.i("TAG", message)
                    mNewRecipesApiState.value = ErrorState(message)
                }
                .onException {
                    it.localizedMessage?.let { it1 -> Log.i("TAG", it1) }
                }
        }
    }

    private fun convertNewRecipeResponseToUIModel(
        recipes: List<SingleNewRecipe>
    ) {
        val newRecipeUIList = mutableListOf<NewRecipe>()
        recipes.forEach {
            val newRecipeToBeAdded = NewRecipe(
                id = it.id,
                image = it.image,
                title = it.title,
                readyInMinutes = it.readyInMinutes,
                sourceName = it.sourceName,
                isLoading = false
            )
            newRecipeUIList.add(newRecipeToBeAdded)
        }
        mNewRecipeData.value = newRecipeUIList
    }

    fun searchClicked(fragment: Fragment) {
        homeRoute.toSearch(fragment)
    }
}