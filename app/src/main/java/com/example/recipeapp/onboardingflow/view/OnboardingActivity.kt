package com.example.recipeapp.onboardingflow.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapp.authenticationflow.view.LoginActivity
import com.example.recipeapp.databinding.ActivityOnboardingBinding
import com.example.recipeapp.navigation.NavigationFlows
import com.example.recipeapp.navigation.Navigator
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var userPreference: SharedPreferences
    @Inject
    lateinit var navigation: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userPreference = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        setUpOnboardingComplete()
    }

    private fun setUpOnboardingComplete() {
        binding.apply {
            btnStartCooking.setOnClickListener {
                val editor = userPreference.edit()
                editor.putBoolean("onBoardingDone", true)
                editor.apply()
                if (isUserLoggedIn()) {
                    // TODO: user logged in -> go to Home Screen
                } else {
                    val authFlow = NavigationFlows.AuthenticationFlow(navigation)
                    authFlow.ToLogin().navigate(this@OnboardingActivity)
                    finish()
                }
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val authKey = userPreference.getString("authKey", "")
        return authKey != ""
    }
}