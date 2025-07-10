package com.example.recipeapp.onboardingflow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.recipeapp.navigation.flow.AuthenticationRoute
import com.example.recipeapp.preferences.UserPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authenticationRoute: AuthenticationRoute,
    private val userPreferenceManager: UserPreferenceManager
): ViewModel() {

    fun startCookingButtonClick() {
        authenticationRoute.toLogin()
        userPreferenceManager.setOnboardingDone()
    }
}