package com.example.recipeapp.homeflow.view

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityRecipeSearchBinding
import com.example.recipeapp.savedrecipeflow.view.SearchedRecipeAdapter
import com.example.recipeapp.savedrecipeflow.view.SearchedRecipes

class RecipeSearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecipeSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapter = SearchedRecipeAdapter()
        adapter.changeData(
            listOf(
                SearchedRecipes(
                    id = 1,
                    name = "",
                    chefName = ""
                ),
                SearchedRecipes(
                    id = 2,
                    name = "",
                    chefName = ""
                ),
                SearchedRecipes(
                    id = 3,
                    name = "",
                    chefName = ""
                ),
                SearchedRecipes(
                    id = 4,
                    name = "",
                    chefName = ""
                )
            )
        )
        binding.rvSearchedRecipes.adapter = adapter
        binding.rvSearchedRecipes.layoutManager = GridLayoutManager(
            this,
            2,
            GridLayoutManager.VERTICAL,
            false
        )
        openKeyboard()
    }

    private fun openKeyboard() {
        binding.searchView.requestFocus()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

}