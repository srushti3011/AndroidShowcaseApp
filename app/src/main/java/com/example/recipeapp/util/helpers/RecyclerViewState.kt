package com.example.recipeapp.util.helpers

sealed class RecyclerViewState<T: Any?> {
    class Loading<T: Any?> : RecyclerViewState<T>()
    class Success<T: Any?>(val data: List<T>) : RecyclerViewState<T>()
}