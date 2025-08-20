package com.example.recipeapp.homeflow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.homeflow.model.DietType
import com.example.recipeapp.homeflow.model.MealType
import com.example.recipeapp.homeflow.model.SearchedRecipe
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
        SearchedRecipe(
            id = 715415,
            title = "Red Lentil Soup with Chicken and Turnips",
            image = "https://img.spoonacular.com/recipes/715415-312x231.jpg"
        ),
        SearchedRecipe(
            id = 714606,
            title = "Asparagus and Pea Soup: Real Convenience Food",
            image = "https://img.spoonacular.com/recipes/716406-312x231.jpg"
        ),
        SearchedRecipe(
            id = 644387,
            title = "Garlicky Kale",
            image = "https://img.spoonacular.com/recipes/644387-312x231.jpg"
        ),
        SearchedRecipe(
            id = 715446,
            title = "Slow Cooker Beef Stew",
            image = "https://img.spoonacular.com/recipes/715446-312x231.jpg"
        ),
        SearchedRecipe(
            id = 782601,
            title = "Red Kidney Bean Jambalaya",
            image = "https://img.spoonacular.com/recipes/782601-312x231.jpg"
        ),
        SearchedRecipe(
            id = 716426,
            title = "Cauliflower, Brown Rice, and Vegetable Fried Rice",
            image = "https://img.spoonacular.com/recipes/716426-312x231.jpg"
        ),
        SearchedRecipe(
            id = 716004,
            title = "Quinoa and Chickpea Salad with Sun-Dried Tomatoes and Dried Cherries",
            image = "https://img.spoonacular.com/recipes/716004-312x231.jpg"
        ),
        SearchedRecipe(
            id = 716627,
            title = "Easy Homemade Rice and Beans",
            image = "https://img.spoonacular.com/recipes/716627-312x231.jpg"
        ),
        SearchedRecipe(
            id = 664147,
            title = "Tuscan White Bean Soup with Olive Oil and Rosemary",
            image = "https://img.spoonacular.com/recipes/664147-312x231.jpg"
        ),
        SearchedRecipe(
            id = 640941,
            title = "Crunchy Brussels Sprouts Side Dish",
            image = "https://img.spoonacular.com/recipes/640941-312x231.jpg"
        )
    )
    val recentSearchRecipes: List<SearchedRecipe>
        get() = mRecentSearchRecipes

    private var mSearchRecipeApiState = MutableLiveData<ApiState<ComplexRecipeQueryResponse>>()
    val searchRecipeApiState: LiveData<ApiState<ComplexRecipeQueryResponse>>
        get() = mSearchRecipeApiState

    private var mSearchedRecipeUIModel = MutableLiveData<List<SearchedRecipe>>()
    val searchedRecipesUIModel: LiveData<List<SearchedRecipe>>
        get() = mSearchedRecipeUIModel

    private var mIsRecipeFound = MutableLiveData<Boolean>()
    val isRecipeFound: LiveData<Boolean>
        get() = mIsRecipeFound

    private var mDietTypeFilter = MutableLiveData<MutableSet<DietType>>()
    val dietTypeFilter: LiveData<MutableSet<DietType>>
        get() = mDietTypeFilter

    private var mMealTypeFilter = MutableLiveData<MutableSet<MealType>>()
    val mealTypeFilter: LiveData<MutableSet<MealType>>
        get() = mMealTypeFilter

    init {
        mDietTypeFilter.value = mutableSetOf()
        mMealTypeFilter.value = mutableSetOf()
    }

    fun getRecipesFor(query: String) {
        cancelJob()
        previousSearchRecipeJob = viewModelScope.launch {
            delay(500)
            mSearchRecipeApiState.value = Loading()
            Log.d("Filters", "Diet: ${computeDietType()}, Type: ${computeMealType()}")
            recipeRepository.searchRecipesFor(
                query = query,
                diet = computeDietType(),
                type = computeMealType()
            )
                .onSuccess {
                    mSearchRecipeApiState.value = Success(it)
                    if (it.results.isEmpty()) {
                        mIsRecipeFound.value = false
                    } else {
                        mSearchedRecipeUIModel.value = convertSearchedDataToUIModel(it.results)
                    }
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

    private fun convertSearchedDataToUIModel(
        dataFromApi: List<ComplexQueryResponseSingleRecipe>
    ): List<SearchedRecipe> {
        val uiModel = mutableListOf<SearchedRecipe>()
        dataFromApi.forEach {
            val recipeToBeAdded = SearchedRecipe(
                id = it.id,
                title = it.title,
                image = it.image,
                isLoading = false
            )
            uiModel.add(recipeToBeAdded)
        }
        return uiModel
    }

    fun updateDietType(dietType: DietType, isSelected: Boolean) {
        if (isSelected) {
            mDietTypeFilter.value?.add(dietType)
        } else {
            mDietTypeFilter.value?.remove(dietType)
        }
    }

    fun updateMealType(mealType: MealType, isSelected: Boolean) {
        if (isSelected) {
            mMealTypeFilter.value?.add(mealType)
        } else {
            mMealTypeFilter.value?.remove(mealType)
        }
    }

    private fun computeMealType(): String? {
        return mMealTypeFilter.value?.takeIf { it.isNotEmpty() }
            ?.joinToString(",") { it.toString().lowercase() }
    }

    private fun computeDietType(): String? {
        return mDietTypeFilter.value?.takeIf { it.isNotEmpty() }
            ?.joinToString(",") { it.toString().lowercase() }
    }

    fun areFiltersEmpty(): Boolean {
        return mDietTypeFilter.value.isNullOrEmpty() && mMealTypeFilter.value.isNullOrEmpty()
    }
}