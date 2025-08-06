package com.example.recipeapp.homeflow.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.databinding.RvItemNewRecipeBinding
import com.example.recipeapp.homeflow.model.NewRecipe

class NewRecipeAdapter(
    //private val recipes: Array<NewRecipe>
): RecyclerView.Adapter<NewRecipeAdapter.ViewHolder>() {
    class ViewHolder(
        private val binding: RvItemNewRecipeBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: NewRecipe) {
            binding.apply {
                tvRecipeName.text = recipe.title
                tvSourceName.text = recipe.sourceName
                tvPreparationTime.text = "${recipe.readyInMinutes} mins"
                // render image using glide
            }
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<NewRecipe>() {
        override fun areItemsTheSame(oldItem: NewRecipe, newItem: NewRecipe):
                Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NewRecipe, newItem: NewRecipe):
                Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun changeData(newRecipes: List<NewRecipe>) {
        asyncListDiffer.submitList(newRecipes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RvItemNewRecipeBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList[position])
    }
}