package com.example.recipeapp.model

sealed class LoginInputError {
    data object NotValidEmail : LoginInputError()
    data object EmailEmpty : LoginInputError()
    data object PasswordEmpty : LoginInputError()
    data object EmailAndPasswordEmpty : LoginInputError()
    data object NoError: LoginInputError()
}