package com.thitari.foody.ui.fragments.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.thitari.foody.R
import com.thitari.foody.databinding.FragmentOverviewBinding
import com.thitari.foody.models.Result
import com.thitari.foody.util.Constants
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {
    private val binding by lazy {
        FragmentOverviewBinding.inflate(
            LayoutInflater.from(
                requireContext()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        val bundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)
        binding.ivMainOverview.load(bundle?.image)
        binding.tvTitle.text = bundle?.title
        binding.tvLikes.text = bundle?.aggregateLikes.toString()
        binding.tvTime.text = bundle?.readyInMinutes.toString()
        bundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            binding.tvSummary.text = summary
        }

        if (bundle?.vegetarian == true) {
            binding.ivVegetarian.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.tvVegetarian.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }
        if (bundle?.vegan == true) {
            binding.ivVegan.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.tvVegan.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))
        }
        if (bundle?.glutenFree == true) {
            binding.ivGlutenFree.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.tvGlutenFree.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
        }
        if (bundle?.dairyFree == true) {
            binding.ivDairyFree.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.tvDairyFree.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )

        }
        if (bundle?.dairyFree == true) {
            binding.ivDairyFree.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.tvDairyFree.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )

        }
        if (bundle?.veryHealthy == true) {
            binding.ivHealthy.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green
                )
            )
            binding.tvHealthy.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

        }
        if (bundle?.cheap == true) {
            binding.ivCheap.setColorFilter(ContextCompat.getColor(requireContext(), R.color.green))
            binding.tvCheap.setTextColor(ContextCompat.getColor(requireContext(), R.color.green))

        }

        super.onViewCreated(view, savedInstanceState)
    }
}

