package com.example.recipeapp.network

sealed interface ApiState<T: Any?>
class Idle<T: Any?>: ApiState<T>
class Loading<T: Any?>: ApiState<T>
data class Success<T: Any?>(val response: T): ApiState<T>
data class ErrorState<T: Any?>(val error: String): ApiState<T>