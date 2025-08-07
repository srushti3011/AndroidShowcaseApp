package com.example.recipeapp.homeflow.view

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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
            }

        binding.apply {
            rvCuisines.adapter = cuisineAdapter
            rvCuisines.addItemDecoration(HomeRecyclerViewItemDecoration(dpToPx(30f)))
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
            rvRecipesPerCuisine.addItemDecoration(
                HomeRecyclerViewItemDecoration(dpToPx(30f)
                )
            )
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
            rvNewRecipes.addItemDecoration(HomeRecyclerViewItemDecoration(dpToPx(30f)))
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
                    binding.constraintLayoutRecipePerCuisineFailed.visibility = View.VISIBLE
                    binding.viewRecipePerCuisineLoading.visibility = View.GONE
                    Log.i("TAG", it.toString())
                }
                is Idle -> {}
                is Loading -> {
                    Log.i("TAG", "Loading")
                    applyShimmerEffect(binding.viewRecipePerCuisineLoading)
                    binding.viewRecipePerCuisineLoading.visibility = View.VISIBLE
                    binding.constraintLayoutRecipePerCuisineFailed.visibility = View.GONE
                }
                is Success -> {
                    Log.i("TAG", "Success")
                }
            }
        }

        viewModel.recipePerCuisineData.observe(this) {
            recipePerCuisineAdapter.changeData(it)
            binding.apply {
                constraintLayoutRecipePerCuisineFailed.visibility = View.GONE
                viewRecipePerCuisineLoading.visibility = View.GONE
            }
        }

        viewModel.newRecipeApiState.observe(this) {
            when (it) {
                is ErrorState -> {
                    binding.constraintLayoutNewRecipesFailed.visibility = View.VISIBLE
                    binding.viewNewRecipesLoading.visibility = View.GONE
                    Log.i("TAG", it.toString())
                }
                is Idle -> {}
                is Loading -> {
                    applyShimmerEffect(binding.viewRecipePerCuisineLoading)
                    binding.viewNewRecipesLoading.visibility = View.VISIBLE
                    binding.viewNewRecipeFailed.visibility = View.GONE
                    Log.i("TAG", "Loading")
                }
                is Success -> {
                    Log.i("TAG", "Success")
                }
            }
        }

        viewModel.newRecipeData.observe(this) {
            newRecipeAdapter.changeData(it)
            binding.apply {
                constraintLayoutNewRecipesFailed.visibility = View.GONE
                viewNewRecipesLoading.visibility = View.GONE
            }
        }
    }

    private fun makeInitialApiCall() {
        viewModel.recipeCuisineSet("Indian")
        viewModel.getNewRecipes(10)
    }

    private fun dpToPx(valueInDp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            valueInDp,
            resources.displayMetrics
        ).toInt()
    }

    private fun applyShimmerEffect(view: View) {
        val paint = Paint()
        val gradient = LinearGradient(
            -200f, 0f, 0f, 0f,
            intArrayOf(Color.LTGRAY, Color.WHITE, Color.LTGRAY),
            floatArrayOf(0f, 0.5f, 1f),
            Shader.TileMode.CLAMP
        )

        paint.shader = gradient

        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 1000
        animator.repeatCount = ValueAnimator.INFINITE
        animator.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            val width = view.width.toFloat()

            val animatedGradient = LinearGradient(
                -width + 2 * width * progress, 0f, width * progress, 0f,
                intArrayOf(
                    ContextCompat.getColor(
                    this@HomeActivity, R.color.colorSecondary
                    ),
                    Color.WHITE,
                    ContextCompat.getColor(
                        this@HomeActivity, R.color.colorSecondary
                    ), ),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )

            paint.shader = animatedGradient
            view.background = object : Drawable() {
                override fun draw(canvas: Canvas) {
                    canvas.drawRect(0f, 0f, view.width.toFloat(), view.height.toFloat(), paint)
                }
                override fun setAlpha(alpha: Int) {}
                override fun getOpacity(): Int = PixelFormat.OPAQUE
                override fun setColorFilter(colorFilter: ColorFilter?) {}
            }
        }
        animator.start()
    }

    private fun setupTryAgainFetching() {
        binding.btnRecipePerCuisineTryAgain.setOnClickListener {
            viewModel.recipeCuisineSet(cuisineAdapter.selectedCuisine)
        }
        binding.btnNewRecipeTryAgain.setOnClickListener {
            viewModel.getNewRecipes(10)
        }
    }
}