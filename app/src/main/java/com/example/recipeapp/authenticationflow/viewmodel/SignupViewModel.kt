package com.example.recipeapp.authenticationflow.viewmodel

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.FinishableActivity
import com.example.recipeapp.authenticationflow.model.SignupInputError
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
import kotlinx.coroutines.launch

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val userRepository: UserRespository,
    private val authenticationRoute: AuthenticationRoute,
    private val userPreferenceManager: UserPreferenceManager
) : ViewModel() {

    private val mSignupApiState = MutableLiveData<ApiState<ConnectUserResponse>>()
    val signupApiState: LiveData<ApiState<ConnectUserResponse>>
        get() = mSignupApiState

    private val mSignupInputError = MutableLiveData<SignupInputError>()
    val signupInputError: LiveData<SignupInputError>
        get() = mSignupInputError

    fun validateInput(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        conditionsAccepted: Boolean
    ) {
        val errors = SignupInputError().validate(
            name = name,
            email = email,
            password = password,
            confirmPassword = confirmPassword,
            conditionChecked = conditionsAccepted,
        )
        if (!errors.errorPresent) {
            connectUser(name, email)
            return
        }
        mSignupInputError.value = errors
    }

    private fun connectUser(name: String, email: String) {
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
                    userPreferenceManager.setUserLoggedIn(it.hash)
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

    fun logInClicked(activity: FinishableActivity) {
        authenticationRoute.toLogin(activity)
    }
}