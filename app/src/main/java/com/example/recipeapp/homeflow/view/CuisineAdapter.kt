package com.example.recipeapp.homeflow.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.R
import com.example.recipeapp.databinding.RvItemCuisinesBinding
import com.example.recipeapp.homeflow.model.Cuisine

class CuisineAdapter(
    private val cuisines: Array<Cuisine>,
    private val makeCall: (String) -> Unit
): RecyclerView.Adapter<CuisineAdapter.ViewHolder>() {

    var selectedCuisine = ""

    class ViewHolder(
        private val binding: RvItemCuisinesBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(cuisine: Cuisine, onClickListener: () -> Unit) {
            binding.btnCuisine.text = cuisine.name
            val btnDrawable = if (cuisine.isSelected)
                ContextCompat.getDrawable(binding.root.context, R.drawable.bg_app_button)
            else ContextCompat.getDrawable(binding.root.context, R.drawable.notselected_cuisine)
            binding.btnCuisine.setBackgroundDrawable(btnDrawable)
            if (!cuisine.isSelected) {
                binding.btnCuisine.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorPrimary
                    )
                )
            } else {
                binding.btnCuisine.setTextColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            }
            binding.btnCuisine.setOnClickListener {
                onClickListener()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemBinding = RvItemCuisinesBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return cuisines.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cuisines[position]) {
            val prevSelected = cuisines.indexOfFirst { it.isSelected }
            cuisines[prevSelected].isSelected = false
            cuisines[position].isSelected = true
            selectedCuisine = cuisines[position].name
            notifyItemChanged(prevSelected)
            notifyItemChanged(position)
            makeCall(cuisines[position].name)
        }
    }
}