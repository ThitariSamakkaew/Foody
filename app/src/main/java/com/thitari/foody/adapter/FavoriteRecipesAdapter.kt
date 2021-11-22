package com.thitari.foody.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.thitari.foody.data.database.entities.FavoritesEntity
import com.thitari.foody.databinding.FavoriteRecipesRowLayoutBinding
import com.thitari.foody.util.RecipesDiffUtil

class FavoriteRecipesAdapter : RecyclerView.Adapter<FavoriteRecipesAdapter.FavoriteViewHolder>() {

    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class FavoriteViewHolder(private val binding: FavoriteRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritesEntity: FavoritesEntity) {
            binding.favoriteEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FavoriteViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return FavoriteViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        return FavoriteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val selectRecipes = favoriteRecipes[position]
        holder.bind(selectRecipes)
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    fun setData(newFavoriteRecipes: List<FavoritesEntity>) {
        val favoriteRecipesDiffUtil = RecipesDiffUtil(favoriteRecipes, newFavoriteRecipes)

        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }
}