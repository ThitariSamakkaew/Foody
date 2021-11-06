package com.thitari.foody.viewModel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.thitari.foody.data.DataStoreRepository
import com.thitari.foody.util.Constants.Companion.API_KEY
import com.thitari.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.thitari.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.thitari.foody.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.thitari.foody.util.Constants.Companion.QUERIES_ADD_RECIPE_INFORMATION
import com.thitari.foody.util.Constants.Companion.QUERIES_API_KEY
import com.thitari.foody.util.Constants.Companion.QUERIES_DIET
import com.thitari.foody.util.Constants.Companion.QUERIES_FILL_INGREDIENTS
import com.thitari.foody.util.Constants.Companion.QUERIES_NUMBER
import com.thitari.foody.util.Constants.Companion.QUERIES_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false
    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    fun saveBackOnline(backOnline: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreRepository.saveBackOnline(backOnline)
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        viewModelScope.launch {
            readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }
        queries[QUERIES_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERIES_API_KEY] = API_KEY
        queries[QUERIES_TYPE] = mealType
        queries[QUERIES_DIET] = dietType
        queries[QUERIES_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERIES_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We are back online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }
}
