package com.example.recipeapp.authenticationflow.model

import android.util.Patterns

class LoginInputError(
    var emailErrorType: ErrorType? = null,
    var passwordErrorType: ErrorType? = null
) {
    val errorPresent: Boolean
        get() = listOf(
            emailErrorType,
            passwordErrorType
        ).any { it != null }

    fun validate(
        email: String,
        password: String
    ): LoginInputError {
        val emailError = when {
            email.isEmpty() -> ErrorType.FieldIsEmptyError("Email is empty")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                ErrorType.ValidationError("Email is not valid")
            else -> null
        }

        val passwordError = when {
            password.isEmpty() -> ErrorType.FieldIsEmptyError("Password is empty")
            else -> null
        }

        return LoginInputError(
            emailError,
            passwordError
        )
    }
}