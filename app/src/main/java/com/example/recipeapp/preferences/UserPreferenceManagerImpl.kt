package com.example.recipeapp.preferences

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class UserPreferenceManagerImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
): UserPreferenceManager {

    private val userPreference: SharedPreferences = applicationContext.getSharedPreferences(
        "UserPreferences",
        MODE_PRIVATE
    )

    private val editor: Editor = userPreference.edit()

    override fun setOnboardingDone() {
        editor.putBoolean("onBoardingDone", true)
        editor.apply()
    }

    override fun isOnboardingDone(): Boolean {
        return userPreference.getBoolean("onBoardingDone", false)
    }

    override fun isUserLoggedIn(): Boolean {
        val authKey = userPreference.getString("authKey", "")
        return authKey != ""
    }

    override fun setUserLoggedIn(key: String) {
        editor.putString("authKey", key)
        editor.apply()
    }

    override fun setUserLoggedOut() {
        editor.remove("authKey")
        editor.apply()
    }
}