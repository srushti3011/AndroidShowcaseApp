package com.example.recipeapp.onboardingflow.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeapp.databinding.ActivityOnboardingBinding
import com.example.recipeapp.onboardingflow.viewmodel.OnboardingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingActivity: AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpOnboardingComplete()
    }

    private fun setUpOnboardingComplete() {
        binding.apply {
            btnStartCooking.setOnClickListener {
                viewModel.startCookingButtonClick()
            }
        }
    }
}