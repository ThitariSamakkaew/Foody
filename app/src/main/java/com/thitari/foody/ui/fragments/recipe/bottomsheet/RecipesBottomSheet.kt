package com.thitari.foody.ui.fragments.recipe.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.thitari.foody.databinding.RecipesBottomSheetBinding
import com.thitari.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.thitari.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.thitari.foody.viewModel.RecipesViewModel
import java.util.*

class RecipesBottomSheet : BottomSheetDialogFragment() {

    private lateinit var recipesViewModel: RecipesViewModel
    private val binding by lazy {
        RecipesBottomSheetBinding.inflate(
            LayoutInflater.from(
                requireContext()
            )
        )
    }

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipesViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding.chipGroupMealType.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)
            mealTypeChip = selectedMealType
            mealTypeChipId = selectedChipId
        }

        binding.chipGroupDietType.setOnCheckedChangeListener { group, selectedChipId ->
            val chip = group.findViewById<Chip>(selectedChipId)
            val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)
            dietTypeChip = selectedDietType
            dietTypeChipId = selectedChipId
        }

        binding.btnApply.setOnClickListener {
            recipesViewModel.saveMealAndDietType(
                mealType = mealTypeChip,
                mealTypeId = mealTypeChipId,
                dietType = dietTypeChip,
                dietTypeId = dietTypeChipId
            )
            val action =
                RecipesBottomSheetDirections.actionRecipesBottomSheetToRecipesFragment()
            action.backFromBottomSheet = true
            findNavController().navigate(action)
        }

        return binding.root
    }
}