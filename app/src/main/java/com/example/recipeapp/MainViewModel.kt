package com.example.recipeapp

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.recipeapp.navigation.route.AuthenticationRoute
import com.example.recipeapp.navigation.route.OnboardingRoute
import com.example.recipeapp.preferences.UserPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val onboardingRoute: OnboardingRoute,
    private val authenticationRoute: AuthenticationRoute,
    private val userPreferenceManager: UserPreferenceManager
) : ViewModel() {

    fun navigateToStartScreen(activity: FinishableActivity) {
        if (userPreferenceManager.isOnboardingDone()) {
            if (userPreferenceManager.isUserLoggedIn()) {
                // TODO: user logged in -> go to Home Screen
            } else {
                authenticationRoute.toLogin(activity)
            }
        } else {
            onboardingRoute.toOnboarding(activity)
        }
    }
}