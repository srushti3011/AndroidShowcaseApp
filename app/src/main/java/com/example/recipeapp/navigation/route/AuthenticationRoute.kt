package com.example.recipeapp.navigation.route

import android.app.Activity

interface AuthenticationRoute {
    fun toLogin(activity: Activity)
    fun toSignup(activity: Activity)
}