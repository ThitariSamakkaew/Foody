package com.thitari.foody.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.thitari.foody.R
import com.thitari.foody.models.Result
import com.thitari.foody.util.Constants
import kotlinx.android.synthetic.main.fragment_instructions.view.*

class InstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_instructions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments
        val bundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        view.webViewInstructions.webViewClient = object : WebViewClient() {}
        val websiteUrl: String = bundle!!.sourceUrl
        view.webViewInstructions.loadUrl(websiteUrl)
        super.onViewCreated(view, savedInstanceState)
    }
}