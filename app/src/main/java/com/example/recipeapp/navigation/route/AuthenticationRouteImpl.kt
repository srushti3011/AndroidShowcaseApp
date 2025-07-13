package com.example.recipeapp.navigation.route

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.recipeapp.authenticationflow.view.LoginActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class AuthenticationRouteImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
): AuthenticationRoute {

    override fun toLogin(activity: Activity) {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.apply {
            startActivity(intent)
            finish()
        }
    }

    override fun toSignup(activity: Activity) {}
}