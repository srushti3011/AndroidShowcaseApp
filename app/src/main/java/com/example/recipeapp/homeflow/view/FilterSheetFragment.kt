package com.example.recipeapp.homeflow.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.recipeapp.databinding.FragmentFilterSheetBinding
import com.example.recipeapp.homeflow.model.DietType
import com.example.recipeapp.homeflow.model.MealType
import com.example.recipeapp.homeflow.viewmodel.RecipeSearchViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FilterSheetFragment(
    val currentQuery: String,
    val bringToRecentsState: () -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterSheetBinding
    private val viewModel: RecipeSearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.dietTypeFilter.value?.forEach {
            when (it) {
                DietType.VEG -> {
                    binding.btnDietTypeFilterVeg.isSelected = true
                }
                DietType.VEGAN -> {
                    binding.btnDietTypeFilterVegan.isSelected = true
                }
            }
        }
        viewModel.mealTypeFilter.value?.forEach {
            when (it) {
                MealType.MAINCOURSE -> {
                    binding.btnMealTypeFilterMainCourse.isSelected = true
                }
                MealType.BREAKFAST -> {
                    binding.btnMealTypeFilterBreakfast.isSelected = true
                }
                MealType.SIDEDISH -> {
                    binding.btnMealTypeFilterSideDish.isSelected = true
                }
            }
        }
        binding.apply {
            arrayOf(
                btnDietTypeFilterVeg,
                btnDietTypeFilterVegan,
                btnMealTypeFilterBreakfast,
                btnMealTypeFilterSideDish,
                btnMealTypeFilterMainCourse
            ).forEach { btn ->
                btn.setOnClickListener {
                    it.isSelected = !it.isSelected
                    if (it == btnDietTypeFilterVegan) {
                        viewModel.updateDietType(DietType.VEGAN, it.isSelected)
                    }
                    if (it == btnDietTypeFilterVeg) {
                        viewModel.updateDietType(DietType.VEG, it.isSelected)
                    }
                    if (it == btnMealTypeFilterBreakfast) {
                        viewModel.updateMealType(MealType.BREAKFAST, it.isSelected)
                    }
                    if (it == btnMealTypeFilterSideDish) {
                        viewModel.updateMealType(MealType.SIDEDISH, it.isSelected)
                    }
                    if (it == btnMealTypeFilterMainCourse) {
                        viewModel.updateMealType(MealType.MAINCOURSE, it.isSelected)
                    }
                }
            }
        }

        binding.btnApplyFilter.setOnClickListener {
            if (currentQuery.isEmpty() && viewModel.areFiltersEmpty()) {
                bringToRecentsState()
            } else {
                viewModel.getRecipesFor(currentQuery)
            }
            this@FilterSheetFragment.dismiss()
        }
    }
}