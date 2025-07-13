package com.example.recipeapp.authenticationflow.model

sealed class ErrorType {
    data class ValidationError(val message: String) : ErrorType()
    data class FieldIsEmptyError(val message: String) : ErrorType()
}