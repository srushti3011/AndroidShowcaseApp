package com.example.recipeapp.savedrecipeflow.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.RvItemSearchedRecipeBinding

class SearchedRecipeAdapter: RecyclerView.Adapter<SearchedRecipeAdapter.ViewHolder>() {

    class ViewHolder(
        val binding: RvItemSearchedRecipeBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind() {}
    }

    private val diffUtil = object : DiffUtil.ItemCallback<SearchedRecipes>() {
        override fun areItemsTheSame(oldItem: SearchedRecipes, newItem: SearchedRecipes):
                Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SearchedRecipes, newItem: SearchedRecipes):
                Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun changeData(newRecipes: List<SearchedRecipes>) {
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
        holder.bind()
    }
}

data class SearchedRecipes(
    val id: Int,
    val name: String,
    val chefName: String
)