package com.example.recipeapp.authenticationflow.viewmodel

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.network.ApiState
import com.example.recipeapp.network.ErrorState
import com.example.recipeapp.network.Loading
import com.example.recipeapp.authenticationflow.model.SignupInputDetailEmptyFields
import com.example.recipeapp.authenticationflow.model.SignupInputError
import com.example.recipeapp.network.Success
import com.example.recipeapp.network.networkmodel.ConnectUserBody
import com.example.recipeapp.network.networkmodel.ConnectUserResponse
import com.example.recipeapp.network.networkrepository.UserRespository
import com.example.recipeapp.network.onError
import com.example.recipeapp.network.onException
import com.example.recipeapp.network.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userRepository: UserRespository
) : ViewModel() {
    private val mSignupInputErrorState = MutableLiveData<SignupInputError>()
    val signupInputErrorState: LiveData<SignupInputError>
        get() = mSignupInputErrorState

    private val mSignupApiState = MutableLiveData<ApiState<ConnectUserResponse>>()
    val signupApiState: LiveData<ApiState<ConnectUserResponse>>
        get() = mSignupApiState

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
            connectUser(name, email)
        }
    }

    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun connectUser(name: String, email: String) {
        val connectUserBody = ConnectUserBody(
            username = name,
            firstName = "defaultFirstName",
            lastName = "defaultLastName",
            email = email
        )
        viewModelScope.launch {
            mSignupApiState.value = Loading()
            userRepository.connectUser(connectUserBody)
                .onSuccess {
                    Log.i("TAG", it.toString())
                    mSignupApiState.value = Success(it)
                }
                .onError { code, message ->
                    mSignupApiState.value = ErrorState("$message with $code")
                }
                .onException {
                    mSignupApiState.value = it.localizedMessage?.let { it1 -> ErrorState(it1) }
                }
        }
    }
}