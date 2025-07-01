package com.example.recipeapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.recipeapp.model.LoginInputError

class LoginViewModel: ViewModel() {

    private var mLoginErrorState = MutableLiveData<LoginInputError>()
    val loginErrorState: LiveData<LoginInputError>
        get() = mLoginErrorState

    fun validateInput(email: String, password: String) {
        if (email.isEmpty() && password.isEmpty()) {
            mLoginErrorState.value = LoginInputError.EmailAndPasswordEmpty
        } else if (email.isEmpty()) {
            mLoginErrorState.value = LoginInputError.EmailEmpty
        } else if (password.isEmpty()) {
            mLoginErrorState.value = LoginInputError.PasswordEmpty
        } else if (!validateEmail(email)) {
            mLoginErrorState.value = LoginInputError.NotValidEmail
        } else {
            mLoginErrorState.value = LoginInputError.NoError
        }
    }

    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}