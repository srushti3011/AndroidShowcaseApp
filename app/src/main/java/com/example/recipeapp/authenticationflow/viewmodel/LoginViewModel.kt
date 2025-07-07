package com.example.recipeapp.authenticationflow.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.network.ApiState
import com.example.recipeapp.network.ErrorState
import com.example.recipeapp.network.Loading
import com.example.recipeapp.authenticationflow.model.LoginInputError
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
class LoginViewModel @Inject constructor(
    private val userRepository: UserRespository
) : ViewModel() {

    private var mLoginErrorState = MutableLiveData<LoginInputError>()
    val loginErrorState: LiveData<LoginInputError>
        get() = mLoginErrorState

    private var mLoginApiState = MutableLiveData<ApiState<ConnectUserResponse>>()
    val loginApiState: LiveData<ApiState<ConnectUserResponse>>
        get() = mLoginApiState

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
            mLoginErrorState.value = LoginInputError.NoError(email, password)
        }
    }

    private fun validateEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun connectUser(email: String, password: String) {
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
                    mLoginApiState.value = Success(it)
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
}