package com.thitari.foody.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.thitari.foody.R
import com.thitari.foody.databinding.IngredientsLowLayoutBinding
import com.thitari.foody.models.ExtendedIngredient
import com.thitari.foody.util.Constants.Companion.BASE_IMAGE_URL
import com.thitari.foody.util.RecipesDiffUtil
import java.util.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class IngredientsViewHolder(val binding: IngredientsLowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        return IngredientsViewHolder(
            IngredientsLowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.binding.ivIngredientsLowLayout.load(BASE_IMAGE_URL + ingredientsList[position].image) {
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        // holder.itemView.tvIngredientsName.text = ingredientsList[position].name
        //  holder.itemView.tvIngredientsId.text = ingredientsList[position].id.toString()
        holder.binding.tvIngredientsName.text =
            ingredientsList[position].name.capitalize(Locale.ROOT)
        holder.binding.tvIngredientAmount.text = ingredientsList[position].amount.toString()
        holder.binding.tvIngredientsUnit.text = ingredientsList[position].unit
        holder.binding.tvIngredientsConsistency.text = ingredientsList[position].consistency
        holder.binding.tvIngredientsOriginal.text = ingredientsList[position].original

    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredients: List<ExtendedIngredient>) {
        val ingredientsDiffUtil = RecipesDiffUtil(ingredientsList, newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}