package com.example.recipeapp.homeflow.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.RvItemRecipePerCuisineBinding
import com.example.recipeapp.homeflow.model.RecipePerCuisine

class RecipePerCuisineAdapter: RecyclerView.Adapter<RecipePerCuisineAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: RvItemRecipePerCuisineBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipePerCuisine) {
            binding.apply {
                tvRecipeName.text = recipe.title
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<RecipePerCuisine>() {
        override fun areItemsTheSame(oldItem: RecipePerCuisine, newItem: RecipePerCuisine):
                Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecipePerCuisine, newItem: RecipePerCuisine):
                Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun changeData(newRecipes: List<RecipePerCuisine>) {
        asyncListDiffer.submitList(newRecipes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemRecipePerCuisineBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }
}