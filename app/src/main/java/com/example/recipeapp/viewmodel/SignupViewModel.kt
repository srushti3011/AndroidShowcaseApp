package com.example.recipeapp.viewmodel

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.model.SignupInputDetailEmptyFields
import com.example.recipeapp.model.SignupInputError

class SignupViewModel: ViewModel() {
    private val mSignupInputErrorState = MutableLiveData<SignupInputError>()
    val signupInputErrorState: LiveData<SignupInputError>
        get() = mSignupInputErrorState

    fun validateInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        conditionsAccepted: Boolean
    ) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            val emptyError = SignupInputError.DetailsEmpty(mutableListOf())
            emptyError.emptyFields.apply {
                if (name.isEmpty()) {
                    add(SignupInputDetailEmptyFields.NAME)
                }
                if (email.isEmpty()) {
                    add(SignupInputDetailEmptyFields.EMAIL)
                }
                if (password.isEmpty()) {
                    add(SignupInputDetailEmptyFields.PASSWORD)
                }
                if (confirmPassword.isEmpty()) {
                    add(SignupInputDetailEmptyFields.CONFPASSWORD)
                }
            }
            mSignupInputErrorState.value = emptyError
        } else if (password != confirmPassword) {
            mSignupInputErrorState.value = SignupInputError.PasswordAndConfirmPasswordNotSame
        } else if (!validateEmail(email)) {
            mSignupInputErrorState.value = SignupInputError.InvalidEmail
        } else if (!conditionsAccepted) {
            mSignupInputErrorState.value = SignupInputError.TermsAndConditionNotAccepted
        } else {
            mSignupInputErrorState.value = SignupInputError.NoError
        }
    }

    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}