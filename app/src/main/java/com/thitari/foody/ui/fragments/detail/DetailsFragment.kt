package com.thitari.foody.ui.fragments.detail

import android.os.Bundle
import android.os.Message
import android.renderscript.ScriptGroup
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.thitari.foody.R
import com.thitari.foody.adapter.PagerAdapter
import com.thitari.foody.data.database.entities.FavoritesEntity
import com.thitari.foody.ui.fragments.ingredients.IngredientsFragment
import com.thitari.foody.ui.fragments.instructions.InstructionsFragment
import com.thitari.foody.ui.fragments.overview.OverviewFragment
import com.thitari.foody.util.Constants
import com.thitari.foody.util.Constants.Companion.RECIPE_RESULT_KEY
import com.thitari.foody.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_details.*
import java.lang.Exception

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private val args by navArgs<DetailsFragmentArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var detailsTabLayout: TabLayout

    private var recipeSave = false
    private var saveRecipeId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detailsTabLayout = view.findViewById(R.id.detailsTabLayout)
        setHasOptionsMenu(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val title = ArrayList<String>()
        title.add("Overview")
        title.add("Ingredients")
        title.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(
            RECIPE_RESULT_KEY,
            args.detailsResult
        ) //args.detailsResult มาจาก argument in my_nav

        val adapter = PagerAdapter(resultBundle, fragments, title, childFragmentManager)
        viewPager.adapter = adapter

        detailsTabLayout.setupWithViewPager(viewPager)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu.findItem(R.id.save_to_favorite_menu)
        checkSaveRecipes(menuItem)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {

        } else if (item.itemId == R.id.save_to_favorite_menu && !recipeSave) {
            saveToFavorite(item)
        } else if (item.itemId == R.id.save_to_favorite_menu && recipeSave) {
            removeFromFavorite(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSaveRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this, { favoriteEntity ->
            try {
                for (saveRecipe in favoriteEntity) {
                    if (saveRecipe.result.recipeId == args.detailsResult.recipeId) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        saveRecipeId = saveRecipe.id
                        recipeSave = true
                    } else {
                        changeMenuItemColor(menuItem, R.color.white)
                    }
                }
            } catch (e: Exception) {
                Log.d("DetailsFragment", e.message.toString())
            }
        })
    }

    private fun saveToFavorite(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(0, args.detailsResult)
        mainViewModel.insertFavoriteRecipes(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe Save.")
        recipeSave = true
    }

    fun removeFromFavorite(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(saveRecipeId, args.detailsResult)
        mainViewModel.deleteFavoriteRecipes(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Remove from Favorite.")
        recipeSave = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(detailsTabLayout, message, Snackbar.LENGTH_SHORT).setAction("Okey") {}.show()
    }

    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(requireContext(), color))

    }
}
