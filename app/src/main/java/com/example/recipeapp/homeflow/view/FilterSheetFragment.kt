package com.example.recipeapp.homeflow.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.recipeapp.databinding.FragmentFilterSheetBinding
import com.example.recipeapp.homeflow.viewmodel.RecipeSearchViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FilterSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentFilterSheetBinding
    private val viewModel: RecipeSearchViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            arrayOf(
                btnDietTypeFilterVeg,
                btnDietTypeFilterNonVeg,
                btnMealTypeFilterBreakfast,
                btnMealTypeFilterSideDish,
                btnMealTypeFilterMainCourse
            ).forEach { btn ->
                btn.setOnClickListener {
                    it.isSelected = !it.isSelected
                }
            }
        }

        binding.btnApplyFilter.setOnClickListener {
            this@FilterSheetFragment.dismiss()
        }

        // TODO: Upon the isSelected value of button, mutate the list in viewmodel;
        // TODO: After mutating that value upon dismissal of the sheet, make call to the search api
        // TODO: and then the data reflection would happen in UI state of SearchActivity's recyclerview
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        Toast.makeText(context, "Sheet closed", Toast.LENGTH_SHORT).show()
    }
}