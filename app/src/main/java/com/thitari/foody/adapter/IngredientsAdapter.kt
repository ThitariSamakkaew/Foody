package com.thitari.foody.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.thitari.foody.R
import com.thitari.foody.models.ExtendedIngredient
import com.thitari.foody.util.Constants.Companion.BASE_IMAGE_URL
import com.thitari.foody.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.ingredients_low_layout.view.*
import java.util.*

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class IngredientsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientsViewHolder {
        return IngredientsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ingredients_low_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: IngredientsViewHolder, position: Int) {
        holder.itemView.ivIngredientsLowLayout.load(BASE_IMAGE_URL + ingredientsList[position].image) {
            crossfade(600)
            error(R.drawable.ic_error_placeholder)
        }
        // holder.itemView.tvIngredientsName.text = ingredientsList[position].name
        //  holder.itemView.tvIngredientsId.text = ingredientsList[position].id.toString()
        holder.itemView.tvIngredientsName.text =
            ingredientsList[position].name.capitalize(Locale.ROOT)
        holder.itemView.tvIngredientAmount.text = ingredientsList[position].amount.toString()
        holder.itemView.tvIngredientsUnit.text = ingredientsList[position].unit
        holder.itemView.tvIngredientsConsistency.text = ingredientsList[position].consistency
        holder.itemView.tvIngredientsOriginal.text = ingredientsList[position].original

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