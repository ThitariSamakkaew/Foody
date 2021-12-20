package com.thitari.foody.ui.fragments.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.thitari.foody.R
import com.thitari.foody.adapter.FavoriteRecipesAdapter
import com.thitari.foody.databinding.FragmentFavoriteRecipesBinding
import com.thitari.foody.viewModel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    private val mainViewModel: MainViewModel by viewModels()

    private val favoriteRecipesAdapter: FavoriteRecipesAdapter by lazy {
        FavoriteRecipesAdapter(requireActivity(), mainViewModel)
    }

    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.favoriteRecipesAdapter = favoriteRecipesAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rvFavoriteRecyclerView)

        setHasOptionsMenu(true)

        setupRecyclerView(recyclerView)

        mainViewModel.readFavoriteRecipes.observe(viewLifecycleOwner, { favoritesEntity ->
            favoriteRecipesAdapter.setData(favoritesEntity)
        })
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll_favorite_recipes_menu) {
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = favoriteRecipesAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun showSnackBar() {
        Snackbar.make(binding.root, "All recipes removed", Snackbar.LENGTH_SHORT)
            .setAction("Okay") {}.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        favoriteRecipesAdapter.clearContextualActionMode()
    }
}