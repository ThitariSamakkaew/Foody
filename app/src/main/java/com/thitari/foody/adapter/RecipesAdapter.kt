package com.thitari.foody.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thitari.foody.databinding.RecipeRowLayoutBinding
import com.thitari.foody.models.FoodRecipe
import com.thitari.foody.models.Result

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.RecipesViewHolder>() {

    private var recipe = emptyList<Result>()

    class RecipesViewHolder(
        private val binding: RecipeRowLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            binding.tvClock.text = result.summary
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecipesViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                return RecipesViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipesViewHolder {
        return RecipesViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        val currentResult = recipe[position]
        holder.bind(currentResult)
    }

    override fun getItemCount(): Int {
        return recipe.size
    }

    fun setData(newData: FoodRecipe) {
        recipe = newData.results
    }
}