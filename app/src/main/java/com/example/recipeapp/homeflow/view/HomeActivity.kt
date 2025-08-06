package com.example.recipeapp.homeflow.view

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.BaseActivity
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityHomeBinding
import com.example.recipeapp.homeflow.viewmodel.HomeViewModel
import com.example.recipeapp.network.ErrorState
import com.example.recipeapp.network.Idle
import com.example.recipeapp.network.Loading
import com.example.recipeapp.network.Success
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding
    private val recipePerCuisineAdapter = RecipePerCuisineAdapter()
    private val newRecipeAdapter = NewRecipeAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setupUI()
        setupObservers()
        makeInitialApiCall()
    }

    private fun setupUI() {
        setupCuisineRecyclerView()
        setupRecipePerCuisineRecyclerView()
        setupNewRecipesRecyclerView()
    }

    private fun setupCuisineRecyclerView() {
        val cuisineAdapter = CuisineAdapter(
                cuisines = viewModel.cuisines
            ) { cuisine ->
                viewModel.recipeCuisineSet(cuisine)
            }

        binding.apply {
            rvCuisines.adapter = cuisineAdapter
            rvCuisines.layoutManager = LinearLayoutManager(
                this@HomeActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun setupRecipePerCuisineRecyclerView() {
        binding.apply {
            recipePerCuisineAdapter.changeData(listOf())
            rvRecipesPerCuisine.adapter = recipePerCuisineAdapter
            rvRecipesPerCuisine.layoutManager = LinearLayoutManager(
                this@HomeActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun setupNewRecipesRecyclerView() {
        binding.apply {
            newRecipeAdapter.changeData(listOf())
            rvNewRecipes.adapter = newRecipeAdapter
            rvNewRecipes.layoutManager = LinearLayoutManager(
                this@HomeActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun setupObservers() {
        viewModel.recipePerCuisineApiState.observe(this) {
            when (it) {
                is ErrorState -> {
                    Log.i("TAG", it.toString())
                }
                is Idle -> {}
                is Loading -> {
                    Log.i("TAG", "Loading")
                }
                is Success -> {
                    Log.i("TAG", "Success")
                }
            }
        }

        viewModel.recipePerCuisineData.observe(this) {
            recipePerCuisineAdapter.changeData(it)
        }

        viewModel.newRecipeApiState.observe(this) {
            when (it) {
                is ErrorState -> {
                    Log.i("TAG", it.toString())
                }
                is Idle -> {}
                is Loading -> {
                    Log.i("TAG", "Loading")
                }
                is Success -> {
                    Log.i("TAG", "Success")
                }
            }
        }

        viewModel.newRecipeData.observe(this) {
            newRecipeAdapter.changeData(it)
        }
    }

    private fun makeInitialApiCall() {
        viewModel.recipeCuisineSet("Indian")
        viewModel.getNewRecipes(10)
    }
}