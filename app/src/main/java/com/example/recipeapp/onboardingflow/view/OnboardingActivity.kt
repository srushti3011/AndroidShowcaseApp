package com.example.recipeapp.onboardingflow.view

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapp.authenticationflow.view.LoginActivity
import com.example.recipeapp.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var userPreference: SharedPreferences

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
                    val intent = Intent(this@OnboardingActivity, LoginActivity::class.java)
                    startActivity(intent)
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