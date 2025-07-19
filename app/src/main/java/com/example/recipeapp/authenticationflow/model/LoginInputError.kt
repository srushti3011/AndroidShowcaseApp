package com.example.recipeapp.authenticationflow.model

class LoginInputError(
    var emailErrorType: ErrorType? = null,
    var passwordErrorType: ErrorType? = null
) {
    val errorPresent: Boolean
        get() = listOf(
            emailErrorType,
            passwordErrorType
        ).any { it != null }
}