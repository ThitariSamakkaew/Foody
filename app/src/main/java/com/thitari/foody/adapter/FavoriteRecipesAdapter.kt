package com.thitari.foody.adapter

import android.app.Activity
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.thitari.foody.R
import com.thitari.foody.data.database.entities.FavoritesEntity
import com.thitari.foody.databinding.FavoriteRecipesRowLayoutBinding
import com.thitari.foody.ui.fragments.favorite.FavoriteRecipesFragmentDirections
import com.thitari.foody.util.RecipesDiffUtil
import com.thitari.foody.viewModel.MainViewModel
import kotlinx.android.synthetic.main.favorite_recipes_row_layout.view.*

class FavoriteRecipesAdapter(
    private val activity: Activity,
    private val mainViewModel: MainViewModel
) :
    RecyclerView.Adapter<FavoriteRecipesAdapter.FavoriteViewHolder>(),
    ActionMode.Callback {

    private var multiSelection = false

    private lateinit var favoriteActionMode: ActionMode
    private lateinit var rootView: View

    private var selectedRecipes = arrayListOf<FavoritesEntity>()

    private var favoriteViewHolders = arrayListOf<FavoriteViewHolder>()

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
        favoriteViewHolders.add(holder)
        rootView = holder.itemView.rootView

        val currentRecipes = favoriteRecipes[position]
        holder.bind(currentRecipes)

        //single click listener
        holder.itemView.favoriteRowLayout.setOnClickListener {

            if (multiSelection) {
                applySelection(holder, currentRecipes)
            } else {
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsFragment(
                        currentRecipes.result
                    )
                holder.itemView.findNavController().navigate(action)
            }
        }
        //long click listener
        holder.itemView.favoriteRowLayout.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                activity.startActionMode(this)
                applySelection(holder, currentRecipes)
                true
            } else {
                multiSelection = false
                false
            }
        }
    }

    private fun applySelection(holder: FavoriteViewHolder, currentRecipes: FavoritesEntity) {
        if (selectedRecipes.contains(currentRecipes)) {
            selectedRecipes.remove(currentRecipes)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
            applyActionModeTitle()
        } else {
            selectedRecipes.add(currentRecipes)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.color_primary)
            applyActionModeTitle()
        }
    }

    private fun changeRecipeStyle(
        holder: FavoriteViewHolder,
        backgroundColor: Int,
        strokeColor: Int
    ) {
        holder.itemView.favoriteRowLayout.setBackgroundColor(
            ContextCompat.getColor(
                activity,
                backgroundColor
            )

        )
        holder.itemView.cvFavoriteRow.strokeColor = ContextCompat.getColor(activity, strokeColor)
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    private fun applyActionModeTitle() {
        when (selectedRecipes.size) {
            0 -> {
                favoriteActionMode.finish()
            }
            1 -> {
                favoriteActionMode.title = "${selectedRecipes.size} item selected"
            }
            else -> {
                favoriteActionMode.title = "${selectedRecipes.size} items selected"
            }
        }
    }

    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)

        favoriteActionMode = actionMode!!

        applyStatusBarColor(R.color.contextualStatusBarColor)

        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        if (menu?.itemId == R.id.delete_favorite_recipes_menu) {
            selectedRecipes.forEach {
                mainViewModel.deleteFavoriteRecipes(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s Removed")

            multiSelection = false
            selectedRecipes.clear()
            actionMode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {
        favoriteViewHolders.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }

        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)

    }

    private fun applyStatusBarColor(color: Int) {
        activity.window.statusBarColor = ContextCompat.getColor(activity, color)

    }

    fun setData(newFavoriteRecipes: List<FavoritesEntity>) {
        val favoriteRecipesDiffUtil = RecipesDiffUtil(favoriteRecipes, newFavoriteRecipes)

        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).setAction("Okay") {}.show()

    }

    fun clearContextualActionMode() {
        if(this :: favoriteActionMode.isInitialized){
            favoriteActionMode.finish()
        }
    }
}