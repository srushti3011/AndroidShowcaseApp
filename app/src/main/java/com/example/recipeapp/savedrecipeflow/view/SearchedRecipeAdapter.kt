package com.example.recipeapp.savedrecipeflow.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.RvItemSearchedRecipeBinding
import com.example.recipeapp.homeflow.model.SearchedRecipe
import com.example.recipeapp.util.helpers.RecyclerViewState
import com.example.recipeapp.util.helpers.ShimmerManager

class SearchedRecipeAdapter: RecyclerView.Adapter<SearchedRecipeAdapter.ViewHolder>() {

    private val dummyLoadingList = listOf(
        SearchedRecipe(
            id = 1,
            title = "sample",
            image = "sample",
            isLoading = true
        ),
        SearchedRecipe(
            id = 2,
            title = "sample",
            image = "sample",
            isLoading = true
        ),
        SearchedRecipe(
            id = 3,
            title = "sample",
            image = "sample",
            isLoading = true
        ),
        SearchedRecipe(
            id = 4,
            title = "sample",
            image = "sample",
            isLoading = true
        ),
        SearchedRecipe(
            id = 5,
            title = "sample",
            image = "sample",
            isLoading = true
        ),
        SearchedRecipe(
            id = 6,
            title = "sample",
            image = "sample",
            isLoading = true
        )
    )

    class ViewHolder(
        val binding: RvItemSearchedRecipeBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: SearchedRecipe) {
            if (recipe.isLoading) {
                ShimmerManager.applyShimmerEffect(binding.loadingShimmerView)
                binding.loadingShimmerView.visibility = View.VISIBLE
            } else {
                binding.loadingShimmerView.visibility = View.GONE
                binding.tvRecipeName.text = recipe.title
                Glide
                    .with(binding.root.context)
                    .load(recipe.image)
                    .into(binding.imgRecipe)
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<SearchedRecipe>() {
        override fun areItemsTheSame(
            oldItem: SearchedRecipe,
            newItem: SearchedRecipe
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: SearchedRecipe,
            newItem: SearchedRecipe)
        : Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun changeData(rvState: RecyclerViewState<SearchedRecipe>) {
        when (rvState) {
            is RecyclerViewState.Loading -> {
                asyncListDiffer.submitList(dummyLoadingList)
            }
            is RecyclerViewState.Success -> {
                asyncListDiffer.submitList(rvState.data)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemSearchedRecipeBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            asyncListDiffer.currentList[position]
        )
    }
}