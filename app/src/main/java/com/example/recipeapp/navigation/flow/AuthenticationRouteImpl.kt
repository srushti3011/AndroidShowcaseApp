package com.example.recipeapp.navigation.flow

import android.content.Context
import android.content.Intent
import com.example.recipeapp.authenticationflow.view.LoginActivity
import com.example.recipeapp.authenticationflow.view.SignUpActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class AuthenticationRouteImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
): AuthenticationRoute {

    override fun toLogin() {
        val intent = Intent(applicationContext, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        applicationContext.startActivity(intent)
    }

    override fun toSignup() {
        val intent = Intent(applicationContext, SignUpActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        applicationContext.startActivity(intent)
    }
}