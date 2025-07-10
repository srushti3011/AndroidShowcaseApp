package com.example.recipeapp.navigation

import android.content.Intent

interface Navigator {
    fun toActivity(intent: Intent)
    fun toFragment()
}