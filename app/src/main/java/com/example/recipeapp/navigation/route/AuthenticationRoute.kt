package com.example.recipeapp.navigation.route

import com.example.recipeapp.FinishableActivity

interface AuthenticationRoute {
    fun toLogin(activity: FinishableActivity)
    fun toSignup(activity: FinishableActivity)
}