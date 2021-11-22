package com.thitari.foody.bindingAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.thitari.foody.adapter.FavoriteRecipesAdapter
import com.thitari.foody.data.database.entities.FavoritesEntity

class FavoriteRecipesBinding {

    companion object {
        @BindingAdapter("viewVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoritesEntity: List<FavoritesEntity>?,
            favoriteRecipesAdapter: FavoriteRecipesAdapter?
        ) {
//
//            val list: List<FavoritesEntity?> = listOf(null, null)
////            val list: List<FavoritesEntity?> = listOf(null)
////            val list2: List<FavoritesEntity>? = null
//            val list2: List<FavoritesEntity>? = listOf(null)
            if (favoritesEntity.isNullOrEmpty()) {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.VISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            } else {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        favoriteRecipesAdapter?.setData(favoritesEntity)
                    }
                }
            }
        }
    }
}