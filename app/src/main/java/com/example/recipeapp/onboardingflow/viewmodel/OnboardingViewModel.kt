package com.example.recipeapp.onboardingflow.viewmodel

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.recipeapp.FinishableActivity
import com.example.recipeapp.navigation.route.AuthenticationRoute
import com.example.recipeapp.preferences.UserPreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authenticationRoute: AuthenticationRoute,
    private val userPreferenceManager: UserPreferenceManager
): ViewModel() {

    fun startCookingButtonClick(activity: FinishableActivity) {
        authenticationRoute.toLogin(activity)
        userPreferenceManager.setOnboardingDone()
    }
}