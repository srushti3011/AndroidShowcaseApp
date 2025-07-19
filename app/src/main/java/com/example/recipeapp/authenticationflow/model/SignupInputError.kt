package com.example.recipeapp.authenticationflow.model

import android.util.Patterns

class SignupInputError(
    var nameError: ErrorType? = null,
    var emailError: ErrorType? = null,
    var passwordError: ErrorType? = null,
    var confirmPasswordError: ErrorType? = null,
    var conditionCheckedError: Boolean = true
) {

    val errorPresent: Boolean
        get() = listOf(
            nameError,
            emailError,
            passwordError,
            confirmPasswordError
        ).any { it != null } || conditionCheckedError

    fun validate(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        conditionChecked: Boolean
    ): SignupInputError {
        val nameError = when {
            name.isEmpty() -> ErrorType.FieldIsEmptyError("Name is empty")
            name.length < 3 -> ErrorType.ValidationError("Name must be at least 3 characters")
            else -> null
        }

        val emailError = when {
            email.isEmpty() -> ErrorType.FieldIsEmptyError("Email is empty")
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                ErrorType.ValidationError("Email is not valid")
            else -> null
        }

        val passwordError = if (password.isEmpty()) {
            ErrorType.FieldIsEmptyError("Password is empty")
        } else null

        val confirmPasswordError = when {
            confirmPassword.isEmpty() ->
                ErrorType.FieldIsEmptyError("Confirm password is empty")
            confirmPassword != password ->
                ErrorType.ValidationError("Confirm password does not match password")
            else -> null
        }

        return SignupInputError(
            nameError,
            emailError,
            passwordError,
            confirmPasswordError,
            !conditionChecked
        )
    }
}