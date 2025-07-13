package com.example.recipeapp.navigation.route

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.example.recipeapp.FinishableActivity
import com.example.recipeapp.authenticationflow.view.LoginActivity
import com.example.recipeapp.authenticationflow.view.SignUpActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class AuthenticationRouteImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
): AuthenticationRoute {

    override fun toLogin(activity: FinishableActivity) {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        activity.apply {
            launchIntent(intent)
            finishActivity()
        }
    }

    override fun toSignup(activity: FinishableActivity) {
        val intent = Intent(applicationContext, SignUpActivity::class.java)
        activity.apply {
            launchIntent(intent)
            finishActivity()
        }
    }
}