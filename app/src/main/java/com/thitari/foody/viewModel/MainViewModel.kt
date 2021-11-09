package com.thitari.foody.viewModel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.thitari.foody.data.Repository
import com.thitari.foody.data.database.RecipesEntity
import com.thitari.foody.models.FoodRecipe
import com.thitari.foody.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** RoomDatabase */
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) { repository.local.insertRecipes(recipesEntity) }

    /** Retrofit */
    val recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    val searchRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) =
        viewModelScope.launch { getRecipesSafeCall(queries) }

    fun searchRecipes(searchQueries: Map<String, String>) =
        viewModelScope.launch { searchRecipesSafeCall(searchQueries) }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            fetchRecipes(queries)
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun fetchRecipes(queries: Map<String, String>) {
        try {
            val response = repository.remote.getRecipes(queries)
            val networkResult = handleFoodRecipesResponse(response)
            recipesResponse.value = networkResult

            if (networkResult is NetworkResult.Success<FoodRecipe>) {
                networkResult.data?.let { foodRecipe ->
                    offlineCatchRecipes(foodRecipe)
                }
            }
        } catch (e: Exception) {
            recipesResponse.value = NetworkResult.Error("Recipe not found")
        }
    }

    private suspend fun searchRecipesSafeCall(searchQueries: Map<String, String>) {
        searchRecipesResponse.value = NetworkResult.Loading()

        if (hasInternetConnection()) {
            fetchSearchRecipes(searchQueries)
        } else {
            searchRecipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun fetchSearchRecipes(searchQueries: Map<String, String>) {
        try {
            val searchResponse = repository.remote.searchRecipes(searchQueries)
            val searchNetworkResponse = handleFoodRecipesResponse(searchResponse)
            searchRecipesResponse.value = searchNetworkResponse
            if (searchNetworkResponse is NetworkResult.Success<FoodRecipe>) {
                searchNetworkResponse.data?.let { searchFoodRecipe ->
                    offlineCatchRecipes(
                        searchFoodRecipe
                    )

                }
            }
        } catch (e: Exception) {
            searchRecipesResponse.value = NetworkResult.Error("Search Recipes not found")
        }
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe> {
        when {
            response.message().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun offlineCatchRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipesEntity)
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<Application>()
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}