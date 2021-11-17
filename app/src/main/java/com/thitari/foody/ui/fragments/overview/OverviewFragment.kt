package com.thitari.foody.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.thitari.foody.R
import com.thitari.foody.models.Result
import com.thitari.foody.util.Constants
import kotlinx.android.synthetic.main.fragment_overview.view.*
import kotlinx.android.synthetic.main.recipes_bottom_sheet.view.*
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        val bundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)
        view.ivMainOverview.load(bundle?.image)
        view.tvTitle.text = bundle?.title
        view.tvLikes.text = bundle?.aggregateLikes.toString()
        view.tvTime.text = bundle?.readyInMinutes.toString()
        bundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            view.tvSummary.text = summary
        }

        if (bundle?.vegetarian == true) {
            view.ivVegetarian.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            view.tvVegetarian.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if (bundle?.vegan == true) {
            view.ivVegan.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            view.tvVegan.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if (bundle?.glutenFree == true) {
            view.ivGlutenFree.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            view.tvGlutenFree.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if (bundle?.dairyFree == true) {
            view.ivDairyFree.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            view.tvDairyFree.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

        }
        if (bundle?.dairyFree == true) {
            view.ivDairyFree.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            view.tvDairyFree.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

        }
        if (bundle?.veryHealthy == true) {
            view.ivHealthy.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            view.tvHealthy.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

        }
        if (bundle?.cheap == true) {
            view.ivCheap.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            view.tvCheap.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

        }

        super.onViewCreated(view, savedInstanceState)
    }

}

