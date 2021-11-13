package com.thitari.foody.ui.fragments.detail

import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayout
import com.thitari.foody.R
import com.thitari.foody.adapter.PagerAdapter
import com.thitari.foody.ui.fragments.ingredients.IngredientsFragment
import com.thitari.foody.ui.fragments.instructions.InstructionsFragment
import com.thitari.foody.ui.fragments.overview.OverviewFragment
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment() {

    private val args by navArgs<DetailsFragmentArgs>()

    private lateinit var detailsTabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        detailsTabLayout = view.findViewById(R.id.detailsTabLayout)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val title = ArrayList<String>()
        title.add("Overview")
        title.add("Ingredients")
        title.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(
            "recipeBundle",
            args.detailsResult
        ) //args.detailsResult มาจาก argument in my_nav

        val adapter = PagerAdapter(resultBundle, fragments, title, childFragmentManager)
        viewPager.adapter = adapter

        detailsTabLayout.setupWithViewPager(viewPager)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
        }
        return super.onOptionsItemSelected(item)
    }
}

