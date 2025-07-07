package com.example.recipeapp.authenticationflow.model

sealed class SignupInputError {
    data class DetailsEmpty(val emptyFields: MutableList<SignupInputDetailEmptyFields>): SignupInputError()
    data object PasswordAndConfirmPasswordNotSame: SignupInputError()
    data object InvalidEmail: SignupInputError()
    data object TermsAndConditionNotAccepted: SignupInputError()
    data object NoError: SignupInputError()
}

enum class SignupInputDetailEmptyFields {
    NAME, EMAIL, PASSWORD, CONFPASSWORD
}