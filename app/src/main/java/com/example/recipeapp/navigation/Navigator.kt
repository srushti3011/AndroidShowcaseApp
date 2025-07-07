package com.example.recipeapp.navigation

import android.app.Activity
import android.content.Context

interface Navigator {
    fun toActivity(context: Context, toActivity: Class<out Activity>)
    fun toFragment()
}