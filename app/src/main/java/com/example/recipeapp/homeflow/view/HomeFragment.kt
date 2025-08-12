package com.example.recipeapp.homeflow.view

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapp.databinding.FragmentHomeBinding
import com.example.recipeapp.homeflow.viewmodel.HomeViewModel
import com.example.recipeapp.network.ErrorState
import com.example.recipeapp.network.Loading
import com.example.recipeapp.util.helpers.RecyclerViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val recipePerCuisineAdapter = RecipePerCuisineAdapter()
    private val newRecipeAdapter = NewRecipeAdapter()
    private lateinit var cuisineAdapter: CuisineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
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
                    requireContext(),
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
                addItemDecoration(HomeRecyclerViewItemDecoration(dpToPx(30f)))
                layoutManager = LinearLayoutManager(
                    requireContext(),
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
                    requireContext(),
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
        viewModel.recipePerCuisineApiState.observe(viewLifecycleOwner) {
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

        viewModel.recipePerCuisineData.observe(viewLifecycleOwner) {
            binding.apply {
                constraintLayoutRecipePerCuisineFailed.visibility = View.GONE
                recipePerCuisineAdapter.changeData(RecyclerViewState.Success(it))
            }
        }
    }

    private fun setupNewRecipeObservers() {
        viewModel.newRecipeApiState.observe(viewLifecycleOwner) {
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

        viewModel.newRecipeData.observe(viewLifecycleOwner) {
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
