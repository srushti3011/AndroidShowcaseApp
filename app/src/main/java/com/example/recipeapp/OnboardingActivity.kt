package com.example.recipeapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.recipeapp.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var userPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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