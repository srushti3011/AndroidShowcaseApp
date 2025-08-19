package com.example.recipeapp.homeflow.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipeapp.BaseActivity
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityRecipeSearchBinding
import com.example.recipeapp.homeflow.viewmodel.RecipeSearchViewModel
import com.example.recipeapp.network.ErrorState
import com.example.recipeapp.network.Idle
import com.example.recipeapp.network.Loading
import com.example.recipeapp.network.Success
import com.example.recipeapp.savedrecipeflow.view.SearchedRecipeAdapter
import com.example.recipeapp.util.helpers.RecyclerViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipeSearchActivity : BaseActivity() {

    private lateinit var binding: ActivityRecipeSearchBinding
    private val viewModel: RecipeSearchViewModel by viewModels()
    private val adapter = SearchedRecipeAdapter()

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
        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        setupSearchRecyclerView()
        setupSearchView()
        setupFilter()
    }

    private fun setupSearchRecyclerView() {
        adapter.changeData(
            RecyclerViewState.Success(viewModel.recentSearchRecipes)
        )
        binding.rvSearchedRecipes.apply {
            adapter = this@RecipeSearchActivity.adapter
            layoutManager = GridLayoutManager(
                this@RecipeSearchActivity,
                2,
                GridLayoutManager.VERTICAL,
                false
            )
        }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object: OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.apply {
                    cardViewFailedToLoadRecipes.visibility = View.GONE
                    cardViewNoRecipeFound.visibility = View.GONE
                }
                if (!query.isNullOrEmpty()) {
                    currentFocus?.clearFocus()
                    viewModel.getRecipesFor(query)
                } else {
                    adapter.changeData(
                        RecyclerViewState.Success(viewModel.recentSearchRecipes)
                    )
                    binding.apply {
                        tvSearchHeading.text = ContextCompat.getString(
                            this@RecipeSearchActivity,
                            R.string.recent_search
                        )
                        tvSearchResultCount.visibility = View.GONE
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                binding.apply {
                    cardViewFailedToLoadRecipes.visibility = View.GONE
                    cardViewNoRecipeFound.visibility = View.GONE
                }
                if (!newText.isNullOrEmpty()) {
                    viewModel.getRecipesFor(newText)
                } else {
                    adapter.changeData(
                        RecyclerViewState.Success(viewModel.recentSearchRecipes)
                    )
                    binding.apply {
                        tvSearchHeading.text = ContextCompat.getString(
                            this@RecipeSearchActivity,
                            R.string.recent_search
                        )
                        tvSearchResultCount.visibility = View.GONE
                    }
                }
                return true
            }
        })
//        openKeyboard()
    }

    private fun setupFilter() {
        binding.btnFilterSearch.setOnClickListener {
            Log.i("TAG", "filter button clicked")
            val filterSheet = FilterSheetFragment()
            filterSheet.show(supportFragmentManager, "FilterSheet")
        }
    }

    private fun setupObservers() {
        viewModel.searchRecipeApiState.observe(this) {
            when (it) {
                is ErrorState -> {
                    Log.i("TAG", "Error ${it.error}")
                    binding.apply {
                        cardViewFailedToLoadRecipes.visibility = View.VISIBLE
                        cardViewNoRecipeFound.visibility = View.GONE
                    }
                }
                is Idle -> {}
                is Loading -> {
                    binding.apply {
                        cardViewFailedToLoadRecipes.visibility = View.GONE
                        cardViewNoRecipeFound.visibility = View.GONE
                    }
                    adapter.changeData(RecyclerViewState.Loading())
                }
                is Success -> {
                    binding.apply {
                        cardViewFailedToLoadRecipes.visibility = View.GONE

                        tvSearchHeading.text = ContextCompat.getString(
                            this@RecipeSearchActivity,
                            R.string.search_result
                        )
                        tvSearchResultCount.apply {
                            visibility = View.VISIBLE
                            text = getString(
                                R.string.search_result_count,
                                it.response.results.count()
                            )
                        }
                    }
                }
            }
        }

        viewModel.searchedRecipesUIModel.observe(this) {
            adapter.changeData(RecyclerViewState.Success(it))
        }

        viewModel.isRecipeFound.observe(this) {
            if (it) {
                binding.cardViewNoRecipeFound.visibility = View.GONE
            } else {
                binding.cardViewNoRecipeFound.visibility = View.VISIBLE
            }
        }
    }

    private fun openKeyboard() {
        binding.searchView.isIconified = false
        val searchEditText = binding.searchView.findViewById<EditText>(
            androidx.appcompat.R.id.search_src_text
        )
        searchEditText.post {
            searchEditText.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT)
        }
    }
}