package com.thitari.foody.ui.fragments.instructions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.thitari.foody.databinding.FragmentInstructionsBinding
import com.thitari.foody.models.Result
import com.thitari.foody.util.Constants

class InstructionsFragment : Fragment() {

    private val binding by lazy {
        FragmentInstructionsBinding.inflate(
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

        binding.webViewInstructions.webViewClient = object : WebViewClient() {}
        val websiteUrl: String = bundle!!.sourceUrl
        binding.webViewInstructions.loadUrl(websiteUrl)
        super.onViewCreated(view, savedInstanceState)
    }
}