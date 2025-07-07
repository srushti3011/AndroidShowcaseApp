package com.example.recipeapp.authenticationflow.model

sealed class LoginInputError {
    data object NotValidEmail : LoginInputError()
    data object EmailEmpty : LoginInputError()
    data object PasswordEmpty : LoginInputError()
    data object EmailAndPasswordEmpty : LoginInputError()
    data class NoError(val email: String, val password: String): LoginInputError()
}