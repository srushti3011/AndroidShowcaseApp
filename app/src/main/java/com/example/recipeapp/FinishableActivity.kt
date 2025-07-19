package com.example.recipeapp

import android.content.Intent

interface FinishableActivity{

    fun finishActivity()
    fun launchIntent(intent: Intent)
}