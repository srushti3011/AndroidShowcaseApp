package com.example.recipeapp.homeflow.view

import android.os.Bundle
import android.util.TypedValue
import android.view.View
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
import com.example.recipeapp.network.Loading
import com.example.recipeapp.util.helpers.RecyclerViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var binding: ActivityHomeBinding
    private val recipePerCuisineAdapter = RecipePerCuisineAdapter()
    private val newRecipeAdapter = NewRecipeAdapter()
    private lateinit var cuisineAdapter: CuisineAdapter

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
        setupTryAgainFetching()
    }

    private fun setupCuisineRecyclerView() {
        cuisineAdapter = CuisineAdapter(
                cuisines = viewModel.cuisines
            ) { cuisine ->
                viewModel.recipeCuisineSet(cuisine)
                recipePerCuisineAdapter.changeData(RecyclerViewState.Loading())
            }

        binding.apply {
            rvCuisines.apply {
                adapter = cuisineAdapter
                addItemDecoration(HomeRecyclerViewItemDecoration(dpToPx(30f)))
                layoutManager = LinearLayoutManager(
                    this@HomeActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        }
    }

    private fun setupRecipePerCuisineRecyclerView() {
        binding.apply {
            recipePerCuisineAdapter.changeData(RecyclerViewState.Loading())
            rvRecipesPerCuisine.apply {
                adapter = recipePerCuisineAdapter
                addItemDecoration(
                    HomeRecyclerViewItemDecoration(dpToPx(30f)
                    )
                )
                layoutManager = LinearLayoutManager(
                    this@HomeActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        }
    }

    private fun setupNewRecipesRecyclerView() {
        binding.apply {
            newRecipeAdapter.changeData(RecyclerViewState.Loading())
            rvNewRecipes.apply {
                adapter = newRecipeAdapter
                addItemDecoration(HomeRecyclerViewItemDecoration(dpToPx(30f)))
                layoutManager = LinearLayoutManager(
                    this@HomeActivity,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            }
        }
    }

    private fun setupObservers() {
        setupRecipePerCuisineObservers()
        setupNewRecipeObservers()
    }

    private fun setupRecipePerCuisineObservers() {
        viewModel.recipePerCuisineApiState.observe(this) {
            when (it) {
                is ErrorState -> {
                    binding.constraintLayoutRecipePerCuisineFailed.visibility = View.VISIBLE
                }
                is Loading -> {
                    binding.constraintLayoutRecipePerCuisineFailed.visibility = View.GONE
                }
                else -> {}
            }
        }

        viewModel.recipePerCuisineData.observe(this) {
            binding.apply {
                constraintLayoutRecipePerCuisineFailed.visibility = View.GONE
                recipePerCuisineAdapter.changeData(RecyclerViewState.Success(it))
            }
        }
    }

    private fun setupNewRecipeObservers() {
        viewModel.newRecipeApiState.observe(this) {
            when (it) {
                is ErrorState -> {
                    binding.constraintLayoutNewRecipesFailed.visibility = View.VISIBLE
                }
                is Loading -> {
                    binding.constraintLayoutNewRecipesFailed.visibility = View.GONE
                }
                else -> {}
            }
        }

        viewModel.newRecipeData.observe(this) {
            binding.apply {
                constraintLayoutNewRecipesFailed.visibility = View.GONE
            }
            newRecipeAdapter.changeData(RecyclerViewState.Success(it))
        }
    }

    private fun makeInitialApiCall() {
        viewModel.apply {
            recipeCuisineSet("Indian")
            getNewRecipes(10)
        }
    }

    private fun dpToPx(valueInDp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            valueInDp,
            resources.displayMetrics
        ).toInt()
    }

    private fun setupTryAgainFetching() {
        binding.apply {
            btnRecipePerCuisineTryAgain.setOnClickListener {
                viewModel.recipeCuisineSet(cuisineAdapter.selectedCuisine)
            }
            btnNewRecipeTryAgain.setOnClickListener {
                viewModel.getNewRecipes(10)
            }
        }
    }
}