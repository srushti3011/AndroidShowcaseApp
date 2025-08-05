package com.example.recipeapp.homeflow.view

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.BaseActivity
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityHomeBinding
import com.example.recipeapp.homeflow.model.Cuisine
import com.example.recipeapp.homeflow.model.NewRecipe
import com.example.recipeapp.homeflow.model.RecipePerCuisine

class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val cuisines = arrayOf(
        Cuisine(
            name = "Indian",
            isSelected = true
        ),
        Cuisine(
            name = "Italian",
            isSelected = false
        ),
        Cuisine(
            name = "Japanese",
            isSelected = false
        ),
        Cuisine(
            name = "Thai",
            isSelected = false
        ),
        Cuisine(
            name = "Mexican",
            isSelected = false
        )
    )

    private val tempRecipes = arrayOf(
        RecipePerCuisine(
            id = 12,
            title = "Recipe One",
            image = ""
        ),
        RecipePerCuisine(
            id = 14,
            title = "Recipe One",
            image = ""
        ),
        RecipePerCuisine(
            id = 13,
            title = "Recipe One",
            image = ""
        ),
        RecipePerCuisine(
            id = 20,
            title = "Recipe One",
            image = ""
        ),
        RecipePerCuisine(
            id = 22,
            title = "Recipe One",
            image = ""
        )
    )

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
    }

    private fun setupUI() {
        setupCuisineRecyclerView()
        setupRecipePerCuisineRecyclerView()
        setupNewRecipesRecyclerView()
    }

    private fun setupNewRecipesRecyclerView() {
        val newRecipeAdapter = NewRecipeAdapter(
            arrayOf(
                NewRecipe(
                    id = 1,
                    image = "",
                    title = "RecipeOne",
                    readyInMinutes = 20,
                    sourceName = "source 1"
                ),
                NewRecipe(
                    id = 1,
                    image = "",
                    title = "RecipeOne",
                    readyInMinutes = 20,
                    sourceName = "source 1"
                ),
                NewRecipe(
                    id = 1,
                    image = "",
                    title = "RecipeOne",
                    readyInMinutes = 20,
                    sourceName = "source 1"
                ),
                NewRecipe(
                    id = 1,
                    image = "",
                    title = "RecipeOne",
                    readyInMinutes = 20,
                    sourceName = "source 1"
                )
            )
        )
        binding.apply {
            rvNewRecipes.adapter = newRecipeAdapter
            rvNewRecipes.layoutManager = LinearLayoutManager(
                this@HomeActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }

    private fun setupCuisineRecyclerView() {
        val cuisineAdapter = CuisineAdapter(
            cuisines = cuisines
        ) { cuisine ->
            Log.i("TAG", "Making call to $cuisine")
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
        val recipePerCuisineAdapter = RecipePerCuisineAdapter(tempRecipes)
        binding.apply {
            rvRecipesPerCuisine.adapter = recipePerCuisineAdapter
            rvRecipesPerCuisine.layoutManager = LinearLayoutManager(
                this@HomeActivity,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        }
    }
}