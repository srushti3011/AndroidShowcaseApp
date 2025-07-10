package com.example.recipeapp.navigation

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class NavigatorImpl @Inject constructor(
    @ApplicationContext private val context: Context
): Navigator {
    override fun toActivity(intent: Intent) {
        context.startActivity(intent)
    }

    override fun toFragment() {
        // TODO: handle navigation to fragment using directions and nav graph
    }
}