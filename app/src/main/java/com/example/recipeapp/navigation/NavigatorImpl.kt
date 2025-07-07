package com.example.recipeapp.navigation

import android.app.Activity
import android.content.Context
import android.content.Intent
import jakarta.inject.Inject

class NavigatorImpl @Inject constructor(): Navigator {
    override fun toActivity(context: Context, toActivity: Class<out Activity>) {
        val intent = Intent(context, toActivity).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    override fun toFragment() {
        // TODO: handle navigation to fragment using directions and nav graph
    }
}