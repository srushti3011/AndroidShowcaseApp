package com.example.recipeapp.preferences

interface UserPreferenceManager {
    fun setOnboardingDone()
    fun isOnboardingDone(): Boolean
    fun isUserLoggedIn(): Boolean
    fun setUserLoggedIn(key: String)
    fun setUserLoggedOut()
}