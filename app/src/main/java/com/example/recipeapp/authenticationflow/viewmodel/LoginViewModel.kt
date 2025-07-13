package com.example.recipeapp.authenticationflow.viewmodel

import android.app.Activity
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.FinishableActivity
import com.example.recipeapp.authenticationflow.model.ErrorType
import com.example.recipeapp.authenticationflow.model.LoginInputError
import com.example.recipeapp.navigation.route.AuthenticationRoute
import com.example.recipeapp.network.ApiState
import com.example.recipeapp.network.ErrorState
import com.example.recipeapp.network.Loading
import com.example.recipeapp.network.Success
import com.example.recipeapp.network.networkmodel.ConnectUserBody
import com.example.recipeapp.network.networkmodel.ConnectUserResponse
import com.example.recipeapp.network.networkrepository.UserRespository
import com.example.recipeapp.network.onError
import com.example.recipeapp.network.onException
import com.example.recipeapp.network.onSuccess
import com.example.recipeapp.preferences.UserPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRespository,
    private val authenticationRoute: AuthenticationRoute,
    private val userPreferenceManager: UserPreferenceManager
) : ViewModel() {

    private var mLoginApiState = MutableLiveData<ApiState<ConnectUserResponse>>()
    val loginApiState: LiveData<ApiState<ConnectUserResponse>>
        get() = mLoginApiState

    private var mLoginInputError = MutableLiveData<LoginInputError>()
    val loginInputError: LiveData<LoginInputError>
        get() = mLoginInputError

    fun validateInput(email: String, password: String) {
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
        val error = LoginInputError(emailError, passwordError)
        if (!error.errorPresent) {
            connectUser(email, password)
            return
        }
        mLoginInputError.value = error
    }

    private fun connectUser(email: String, password: String) {
        val connectUserBody = ConnectUserBody(
            username = "defaultUserName",
            firstName = "defaultFirstName",
            lastName = "defaultLastName",
            email = email
        )
        viewModelScope.launch {
            mLoginApiState.value = Loading()
            userRepository.connectUser(connectUserBody)
                .onSuccess {
                    delay(30)
                    mLoginApiState.value = Success(it)
                    userPreferenceManager.setUserLoggedIn(it.hash)
                    Log.i("TAG", it.toString())
                }
                .onError { code, message ->
                    mLoginApiState.value = ErrorState("$message with code $code")
                    Log.i("TAG", "$code $message")
                }
                .onException {
                    mLoginApiState.value = it.localizedMessage?.let { it1 -> ErrorState(it1) }
                    Log.i("TAG", it.toString())
                }
        }
    }

    fun signUpClicked(activity: FinishableActivity) {
        authenticationRoute.toSignup(activity)
    }
}