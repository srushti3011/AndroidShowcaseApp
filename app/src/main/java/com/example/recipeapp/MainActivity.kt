package com.example.recipeapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recipeapp.authenticationflow.view.LoginActivity
import com.example.recipeapp.databinding.ActivityMainBinding
import com.example.recipeapp.navigation.NavigationFlows
import com.example.recipeapp.navigation.Navigator
import com.example.recipeapp.onboardingflow.view.OnboardingActivity
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userPreference: SharedPreferences
    @Inject
    lateinit var navigation: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        userPreference = getSharedPreferences("UserPreferences", MODE_PRIVATE)
        setupNavigationFlow()
    }

    private fun setupNavigationFlow() {
        val onboardingDone = userPreference.getBoolean("onBoardingDone", false)
        if (onboardingDone) {
            if (isUserLoggedIn()) {
                // TODO: user logged in -> go to Home Screen
            } else {
                val authFlow = NavigationFlows.AuthenticationFlow(navigation)
                authFlow.ToLogin().navigate(this)
                finish()
            }
        } else {
            val onboardingFlow = NavigationFlows.OnBoardingFlow(navigation)
            onboardingFlow.ToOnboarding().navigate(this@MainActivity)
            finish()
        }
    }

    private fun isUserLoggedIn(): Boolean {
//        val authKey = userPreference.getString("authKey", "")
//        return authKey != ""
        return false
    }
}