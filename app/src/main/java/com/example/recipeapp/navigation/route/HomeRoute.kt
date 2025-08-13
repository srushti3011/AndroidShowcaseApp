package com.example.recipeapp.navigation.route

import androidx.fragment.app.Fragment
import com.example.recipeapp.FinishableActivity

interface HomeRoute {
    fun toHome(activity: FinishableActivity)
    fun toSearch(fragment: Fragment)
}