package com.example.recipeapp

import androidx.lifecycle.ViewModel
import com.example.recipeapp.navigation.flow.AuthenticationRoute
import com.example.recipeapp.navigation.flow.OnboardingRoute
import com.example.recipeapp.preferences.UserPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val onboardingRoute: OnboardingRoute,
    private val authenticationRoute: AuthenticationRoute,
    private val userPreferenceManager: UserPreferenceManager
) : ViewModel() {

    fun navigateToStartScreen() {
        if (userPreferenceManager.isOnboardingDone()) {
            if (userPreferenceManager.isUserLoggedIn()) {
                // TODO: user logged in -> go to Home Screen
            } else {
                authenticationRoute.toLogin()
            }
        } else {
            onboardingRoute.toOnboarding()
        }
    }
}