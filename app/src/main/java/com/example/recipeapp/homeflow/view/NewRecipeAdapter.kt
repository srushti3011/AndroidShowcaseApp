package com.example.recipeapp.homeflow.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.RvItemNewRecipeBinding
import com.example.recipeapp.homeflow.model.NewRecipe

class NewRecipeAdapter(
    private val recipes: Array<NewRecipe>
): RecyclerView.Adapter<NewRecipeAdapter.ViewHolder>() {
    class ViewHolder(
        private val binding: RvItemNewRecipeBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: NewRecipe) {
            binding.apply {
                tvRecipeName.text = recipe.title
                tvSourceName.text = recipe.sourceName
                tvPreparationTime.text = "${recipe.readyInMinutes}"
                // render image using glide
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemNewRecipeBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recipes[position])
    }
}