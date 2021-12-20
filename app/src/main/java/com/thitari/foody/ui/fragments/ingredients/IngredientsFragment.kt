package com.thitari.foody.ui.fragments.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.thitari.foody.adapter.IngredientsAdapter
import com.thitari.foody.databinding.FragmentIngredientsBinding
import com.thitari.foody.models.Result
import com.thitari.foody.util.Constants.Companion.RECIPE_RESULT_KEY

class IngredientsFragment : Fragment() {

    private val ingredientsAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    private val biding by lazy {
        FragmentIngredientsBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return biding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        val bundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)
        biding.rvIngredients.adapter = ingredientsAdapter
        biding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
        bundle?.extendedIngredients?.let { ingredientsAdapter.setData(it) }
    }
    
}