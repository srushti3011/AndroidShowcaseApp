package com.example.recipeapp.savedrecipeflow.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.RvItemSearchedRecipeBinding
import com.example.recipeapp.network.networkmodel.ComplexQueryResponseSingleRecipe

class SearchedRecipeAdapter: RecyclerView.Adapter<SearchedRecipeAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: RvItemSearchedRecipeBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: ComplexQueryResponseSingleRecipe) {
            binding.tvRecipeName.text = recipe.title
            Glide
                .with(binding.root.context)
                .load(recipe.image)
                .into(binding.imgRecipe)
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<ComplexQueryResponseSingleRecipe>() {
        override fun areItemsTheSame(
            oldItem: ComplexQueryResponseSingleRecipe,
            newItem: ComplexQueryResponseSingleRecipe
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ComplexQueryResponseSingleRecipe,
            newItem: ComplexQueryResponseSingleRecipe)
        : Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun changeData(newRecipes: List<ComplexQueryResponseSingleRecipe>) {
        asyncListDiffer.submitList(newRecipes)
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