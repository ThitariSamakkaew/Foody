package com.thitari.foody.ui.fragments.ingredients

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.thitari.foody.R
import com.thitari.foody.adapter.IngredientsAdapter
import com.thitari.foody.models.Result
import com.thitari.foody.ui.fragments.detail.DetailsFragmentArgs
import com.thitari.foody.util.Constants
import com.thitari.foody.util.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_ingredients.view.*

class IngredientsFragment : Fragment() {

    private val ingredientsAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ingredients, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        val bundle: Result? = args?.getParcelable(RECIPE_RESULT_KEY)
        Log.d("TAG", bundle.toString())
        setupRecyclerView(view)
        bundle?.extendedIngredients?.let { ingredientsAdapter.setData(it) }

    }

    private fun setupRecyclerView(view: View) {
        view.rvIngredients.adapter = ingredientsAdapter
        view.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
    }
}