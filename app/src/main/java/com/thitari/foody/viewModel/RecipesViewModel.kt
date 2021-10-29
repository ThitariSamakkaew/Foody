package com.thitari.foody.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.thitari.foody.util.Constants.Companion.API_KEY
import com.thitari.foody.util.Constants.Companion.QUERIES_ADD_RECIPE_INFORMATION
import com.thitari.foody.util.Constants.Companion.QUERIES_API_KEY
import com.thitari.foody.util.Constants.Companion.QUERIES_DIET
import com.thitari.foody.util.Constants.Companion.QUERIES_FILL_INGREDIENTS
import com.thitari.foody.util.Constants.Companion.QUERIES_NUMBER
import com.thitari.foody.util.Constants.Companion.QUERIES_TYPE

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    fun applyQueries(): HashMap<String, String> {

        val queries: HashMap<String, String> = HashMap()
        queries[QUERIES_NUMBER] = "10"
        queries[QUERIES_API_KEY] = API_KEY
        queries[QUERIES_TYPE] = "snack"
        queries[QUERIES_DIET] = "vegan"
        queries[QUERIES_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERIES_FILL_INGREDIENTS] = "true"

        return queries
    }
}