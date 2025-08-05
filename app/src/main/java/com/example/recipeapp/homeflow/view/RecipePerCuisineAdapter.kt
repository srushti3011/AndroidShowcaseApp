package com.example.recipeapp.homeflow.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.RvItemRecipePerCuisineBinding
import com.example.recipeapp.homeflow.model.RecipePerCuisine

class RecipePerCuisineAdapter(
    private val recipes: Array<RecipePerCuisine>
): RecyclerView.Adapter<RecipePerCuisineAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: RvItemRecipePerCuisineBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipePerCuisine) {
            //binding.imgRecipe --> Load image from url
            binding.apply {
                tvRecipeName.text = recipe.title
            }
        }
    }

    // make diff util logic here

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemRecipePerCuisineBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipes[position])
    }
}