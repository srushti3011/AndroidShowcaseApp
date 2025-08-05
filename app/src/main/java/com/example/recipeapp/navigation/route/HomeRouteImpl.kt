package com.example.recipeapp.navigation.route

import android.content.Context
import android.content.Intent
import com.example.recipeapp.FinishableActivity
import com.example.recipeapp.homeflow.view.HomeActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class HomeRouteImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
): HomeRoute {
    override fun toHome(activity: FinishableActivity) {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        activity.apply {
            launchIntent(intent)
            finishActivity()
        }
    }
}