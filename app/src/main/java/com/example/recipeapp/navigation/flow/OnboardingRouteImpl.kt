package com.example.recipeapp.navigation.flow

import android.content.Context
import android.content.Intent
import com.example.recipeapp.onboardingflow.view.OnboardingActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class OnboardingRouteImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
): OnboardingRoute {

    override fun toOnboarding() {
        val intent = Intent(applicationContext, OnboardingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        applicationContext.startActivity(intent)
    }
}