package com.thitari.foody.ui.fragments.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.dataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.thitari.foody.R
import com.thitari.foody.adapter.RecipesAdapter
import com.thitari.foody.util.NetworkResult
import com.thitari.foody.viewModel.MainViewModel
import com.thitari.foody.viewModel.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_recipes.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var recipesViewModel: RecipesViewModel

    private val recipesAdapter by lazy { RecipesAdapter() }

    private lateinit var _view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _view = inflater.inflate(R.layout.fragment_recipes, container, false)
        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        readDatabase()
    }

    private fun setupRecyclerView() {
        _view.recyclerview.adapter = recipesAdapter
        _view.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        showShimmerEffect()
    }

    private fun readDatabase() {

        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { databse ->
                if (databse.isNotEmpty()) {
                    Log.d("RecipesFragment", "readDatabase called!!")
                    recipesAdapter.setData(databse[0].foodRecipes)
                    hideShimmerEffect()
                } else {
                    requestApiData()
                }

            })
        }
    }

    private fun requestApiData() {
        Log.d("RecipesFragment", "requestApiData called!!")
        mainViewModel.getRecipes(recipesViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    hideShimmerEffect()
                    response.data?.let { recipesAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    hideShimmerEffect()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                    showShimmerEffect()
                }
            }
        })
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    recipesAdapter.setData(database[0].foodRecipes)
                }
            })
        }
    }

    private fun showShimmerEffect() {
        _view.recyclerview.showShimmer()
    }

    private fun hideShimmerEffect() {
        _view.recyclerview.hideShimmer()
    }

}
