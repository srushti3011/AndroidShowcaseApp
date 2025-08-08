package com.example.recipeapp.homeflow.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.RvItemRecipePerCuisineBinding
import com.example.recipeapp.homeflow.model.RecipePerCuisine
import com.example.recipeapp.util.helpers.RecyclerViewState
import com.example.recipeapp.util.helpers.ShimmerManager

class RecipePerCuisineAdapter : RecyclerView.Adapter<RecipePerCuisineAdapter.ViewHolder>() {

    private val dummyLoadingList = listOf(
        RecipePerCuisine(
            id = 1,
            title = "sample",
            image = "sample",
            isLoading = true
        ),
        RecipePerCuisine(
            id = 2,
            title = "sample",
            image = "sample",
            isLoading = true
        ),
        RecipePerCuisine(
            id = 3,
            title = "sample",
            image = "sample",
            isLoading = true
        )
    )

    inner class ViewHolder(
        private val binding: RvItemRecipePerCuisineBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: RecipePerCuisine) {
            if (recipe.isLoading) {
                ShimmerManager.applyShimmerEffect(binding.viewLoadingShimmer)
                binding.viewLoadingShimmer.visibility = View.VISIBLE
            } else {
                binding.viewLoadingShimmer.visibility = View.GONE
                binding.tvRecipeName.text = recipe.title
                Glide
                    .with(binding.root.context)
                    .load(recipe.image)
                    .into(binding.imgRecipe)
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

    fun changeData(state: RecyclerViewState<RecipePerCuisine>) {
        when (state) {
            is RecyclerViewState.Loading -> {
                asyncListDiffer.submitList(dummyLoadingList)
            }

            is RecyclerViewState.Success -> {
                asyncListDiffer.submitList(state.data)
            }
        }
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