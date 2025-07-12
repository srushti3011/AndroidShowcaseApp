package com.example.recipeapp.navigation.route

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.recipeapp.FinishableActivity
import com.example.recipeapp.onboardingflow.view.OnboardingActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class OnboardingRouteImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
): OnboardingRoute {

    override fun toOnboarding(activity: FinishableActivity) {
        val intent = Intent(applicationContext, OnboardingActivity::class.java)
        activity.apply {
            launchIntent(intent)
            finishActivity()
        }
    }
}